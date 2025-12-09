document.addEventListener('DOMContentLoaded', function() {
    const months = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
    
    const initialDateInput = document.getElementById('initialDateInput');
    const INITIAL_DATE_KEY = initialDateInput ? initialDateInput.value : '';
    const todayDateInput = document.getElementById('todayDateInput');
    const todayDateKey = todayDateInput ? todayDateInput.value : new Date().toISOString().split('T')[0];
    
    let date = new Date();
    let currentYear = date.getFullYear();
    let currentMonth = date.getMonth();
    let selectedDay = date.getDate();
    let eventsByDate = {};
    let allEvents = [];
    let categoriasMap = {};
    let currentView = 'mensal';
    
    if (INITIAL_DATE_KEY) {
        try {
            const parts = INITIAL_DATE_KEY.split('-');
            if (parts.length === 3) {
                currentYear = parseInt(parts[0]);
                currentMonth = parseInt(parts[1]) - 1;
                selectedDay = parseInt(parts[2]);
                date = new Date(currentYear, currentMonth, selectedDay);
            }
        } catch (e) {
            console.error("Erro ao inicializar data do input:", e);
        }
    }
    
    const monthListEl = document.getElementById('monthList');
    const daysGridEl = document.getElementById('daysGrid');
    const yearDisplayEl = document.getElementById('currentYear');
    const monthHeaderEl = document.getElementById('monthHeader');
    const selectedDateDisplayEl = document.getElementById('selectedDateDisplay');
    const eventListEl = document.getElementById('eventList');
    const eventDataInput = document.getElementById('eventDataInput');
    const categoriesDataInput = document.getElementById('categoriesDataInput');
    const eventForm = document.getElementById('eventForm');
    const addEventButton = document.getElementById('addEventButton');
    const formContainer = document.getElementById('eventFormContainer');
    const fileInput = document.getElementById('anexoFile');
    const fileNameDisplay = document.getElementById('fileNameDisplay');
    const anexoUrlInput = document.getElementById('anexoUrl');
    const viewSwitcher = document.getElementById('viewSwitcher');
    const prevYearBtn = document.getElementById('prevYear');
    const nextYearBtn = document.getElementById('nextYear');
    const cancelButton = document.getElementById('cancelButton');
    
    const fileLabel = document.querySelector('.file-input-wrapper .file-label');

    function getDaysInMonth(year, month) {
        return new Date(year, month + 1, 0).getDate();
    }
    
    function getFirstDayOfMonth(year, month) {
        return new Date(year, month, 1).getDay();
    }
    
    function formatDateKey(year, month, day) {
        const m = String(month + 1).padStart(2, '0');
        const d = String(day).padStart(2, '0');
        return `${year}-${m}-${d}`;
    }
    
    function isDateInPast(dateKey) {
        return dateKey < todayDateKey;
    }
    
    function getSelectedDateKey() {
        return formatDateKey(currentYear, currentMonth, selectedDay);
    }
    
    function toggleFormVisibility(visible, isEdit = false) {
        if (!formContainer) return;
        formContainer.style.display = visible ? 'block' : 'none';
        const formAcao = document.getElementById('formAcao');
        if (formAcao) formAcao.value = isEdit ? 'editar' : 'cadastrar';
        if (!visible) {
            resetForm();
        }
    }
    
    function resetForm(dataEventoKey = getSelectedDateKey()) {
        if (!eventForm) return;
        eventForm.reset();
        const dataEventoEl = document.getElementById('dataEvento');
        const idEventoEl = document.getElementById('idEvento');
        const formAcaoEl = document.getElementById('formAcao');
        
        if (dataEventoEl) dataEventoEl.value = dataEventoKey;
        if (idEventoEl) idEventoEl.value = '';
        if (formAcaoEl) formAcaoEl.value = 'cadastrar';
        if (fileNameDisplay) fileNameDisplay.textContent = '';
        if (anexoUrlInput) anexoUrlInput.disabled = false;
        
        const dataAtualInput = eventForm.querySelector('input[name="dataAtual"]');
        if (dataAtualInput) dataAtualInput.value = getSelectedDateKey();
    }

    function preprocessEvents() {
        try {
            const rawData = eventDataInput ? eventDataInput.value : '[]';
            const data = JSON.parse(rawData);
            eventsByDate = {};
            allEvents = data;
            
            data.forEach(event => {
                if (!event.dataEvento) return;
                
                const startDate = new Date(event.dataEvento + 'T00:00:00');
                const tipoRepeticao = event.tipoRepeticao; 
                const endDateDuration = event.dataFim ? new Date(event.dataFim + 'T00:00:00') : null;
                
                let limitDate = new Date(startDate);
                
                if (tipoRepeticao !== 'NENHUM') {
                    limitDate.setFullYear(startDate.getFullYear() + 1);
                    if (endDateDuration && endDateDuration > startDate && endDateDuration < limitDate) {
                         limitDate = endDateDuration; 
                    }
                } else {
                    limitDate = endDateDuration || startDate; 
                }

                let currentOccurrenceDate = new Date(startDate);
                const originalDay = startDate.getDate();
                const originalDayOfWeek = startDate.getDay(); 

                let safeCounter = 0; 
                const MAX_ITERATIONS = tipoRepeticao === 'NENHUM' ? 1 : 366; 
                
                while (currentOccurrenceDate <= limitDate && safeCounter < MAX_ITERATIONS) {
                    const dateKey = formatDateKey(currentOccurrenceDate.getFullYear(), currentOccurrenceDate.getMonth(), currentOccurrenceDate.getDate());
                    
                    let isValidOccurrence = true;

                    if (safeCounter > 0) {
                        if (tipoRepeticao === 'SEMANAL' && currentOccurrenceDate.getDay() !== originalDayOfWeek) {
                            isValidOccurrence = false; 
                        }

                    }

                    if (isValidOccurrence) {
                        if (!eventsByDate[dateKey]) {
                            eventsByDate[dateKey] = [];
                        }
                        eventsByDate[dateKey].push({
                            ...event, 
                            dataExibicao: dateKey,
                            isRepetition: tipoRepeticao !== 'NENHUM'
                        });
                    }
                    
                    const day = currentOccurrenceDate.getDate();
                    const month = currentOccurrenceDate.getMonth();
                    const year = currentOccurrenceDate.getFullYear();
                    
                    switch (tipoRepeticao) {
                        case 'DIARIO':
                            currentOccurrenceDate.setDate(day + 1);
                            break;
                        case 'SEMANAL':
                            currentOccurrenceDate.setDate(day + 7);
                            break;
                        case 'MENSAL':

                            let nextMonthDate = new Date(year, month + 1, 1);

                            currentOccurrenceDate = new Date(year, month + 1, originalDay);

                            if (currentOccurrenceDate.getDate() !== originalDay) {
                                currentOccurrenceDate = new Date(year, month + 1, 0); 
                            }
                            
                            break;
                        case 'ANUAL':
                            currentOccurrenceDate.setFullYear(year + 1);
                            break;
                        default: 
                            safeCounter = MAX_ITERATIONS;
                            break;
                    }
                    safeCounter++;
                }
            });
        } catch (e) {
            console.error("Erro no preprocessEvents:", e);
            eventsByDate = {};
            allEvents = [];
        }
    }

    
    function loadCategories() {
        try {
            const rawCats = categoriesDataInput ? categoriesDataInput.value : '[]';
            const categoriasFromJSP = JSON.parse(rawCats);
            categoriasMap = categoriasFromJSP.reduce((acc, cat) => {
                acc[cat.id] = cat;
                return acc;
            }, {});
        } catch (e) {
            console.error("Erro ao carregar categorias:", e);
            categoriasMap = {};
        }
    }
    
    function renderCalendar() {
        loadCategories();
        preprocessEvents();
        
        if (monthHeaderEl) monthHeaderEl.textContent = `${months[currentMonth].toUpperCase()} ${currentYear}`;
        if (yearDisplayEl) yearDisplayEl.textContent = currentYear;
        if (daysGridEl) daysGridEl.innerHTML = '';
        
        const daysInMonth = getDaysInMonth(currentYear, currentMonth);
        const firstDay = getFirstDayOfMonth(currentYear, currentMonth);
        
        if (monthListEl) {
             monthListEl.querySelectorAll('li').forEach((li, index) => {
                 li.classList.remove('active');
                 if (index === currentMonth) {
                     li.classList.add('active');
                 }
             });
        }
        
        if (daysGridEl) {
            for (let i = 0; i < firstDay; i++) {
                daysGridEl.innerHTML += '<div class="day day-empty"></div>';
            }
            
            for (let day = 1; day <= daysInMonth; day++) {
                const dateKey = formatDateKey(currentYear, currentMonth, day);
                const isToday = dateKey === todayDateKey;
                const isSelected = day === selectedDay;
                const events = eventsByDate[dateKey] || [];
                
                let dayClass = 'day';
                if (isToday) dayClass += ' today';
                if (isSelected) dayClass += ' selected';
                if (isDateInPast(dateKey)) dayClass += ' past-day';
                
                const dayEl = document.createElement('div');
                dayEl.className = dayClass;
                dayEl.dataset.date = dateKey;
                dayEl.textContent = day;
                
                if (events.length > 0) {
                    const countSpan = document.createElement('span');
                    countSpan.className = 'day-event-count';
                    countSpan.textContent = events.length;
                    dayEl.appendChild(countSpan);
                }
                
                dayEl.onclick = () => {
                    selectedDay = day;
                    renderCalendar(); 
                    renderEventList(dateKey, eventsByDate[dateKey] || []);
                    toggleFormVisibility(false);
                    resetForm(dateKey);
                };
                
                daysGridEl.appendChild(dayEl);
            }
        }
        
        renderEventList(getSelectedDateKey(), eventsByDate[getSelectedDateKey()] || []);
    }
    
    function renderEventList(dateKey, events) {
        if (selectedDateDisplayEl) selectedDateDisplayEl.textContent = `Eventos em ${dateKey.split('-').reverse().join('/')}`;
        if (!eventListEl) return;
        eventListEl.innerHTML = '';
        
        if (events.length > 0) {
            events.sort((a, b) => (a.horario || '23:59').localeCompare(b.horario || '23:59'));
            
            events.forEach(evt => {
                const category = categoriasMap[evt.categoria.id] || { nome: 'Outros', corHex: '#888', iconeCss: 'fa-solid fa-circle' };
                const card = document.createElement('div');
                card.className = 'event-card';
                card.style.borderLeftColor = category.corHex;
                
                const content = `
                    <div class="event-header">
                        <h4>
                            <i class="${category.iconeCss}"></i>
                            ${evt.titulo}
                            ${evt.horario ? `<span class="event-horario"> (${evt.horario.substring(0, 5)})</span>` : ''}
                        </h4>
                        <div class="event-actions" data-id="${evt.id}">
                            ${!evt.isRepetition ? `<a href="#" class="edit-event-btn" data-id="${evt.id}"><i class="fa-solid fa-pen-to-square"></i></a>` : ''}
                            
                            <a href="${typeof CONTEXT_PATH !== 'undefined' ? CONTEXT_PATH : ''}/CronogramaController?acao=excluir&id=${evt.id}&dataAtual=${getSelectedDateKey()}"
                               class="delete-event-btn"
                               onclick="return confirm('Tem certeza que deseja excluir o evento: ${evt.titulo.replace(/'/g, "\\'")}?');">
                                    <i class="fa-solid fa-trash-can"></i>
                            </a>
                        </div>
                    </div>
                    <p class="event-category" style="color:${category.corHex};">Categoria: ${category.nome}</p>
                    <p class="event-description">${evt.descricao}</p>
                    ${evt.dataFim ? `<p class="event-duration">Duração: ${evt.dataEvento.split('-').reverse().join('/')} até ${evt.dataFim.split('-').reverse().join('/')}</p>` : ''}
                    ${evt.tipoRepeticao !== 'NENHUM' ? `<p class="event-repetition">Repetição: ${evt.tipoRepeticao} ${evt.isRepetition ? '(Ocorrência)' : ''}</p>` : ''}
                    ${evt.anexoUrl ? `<p class="event-anexo"><a href="${evt.anexoUrl}" target="_blank"><i class="fa-solid fa-link"></i> Anexo / Link</a></p>` : ''}
                    
                    <div class="event-log">
                        Criado por: ${evt.autor ? evt.autor.nome : 'N/A'} em ${evt.dataCriacao ? evt.dataCriacao.split('-').reverse().join('/') : 'N/A'}<br>
                        ${evt.editor ? `Última edição por: ${evt.editor.nome} em ${evt.dataUltimaEdicao.split('-').reverse().join('/')}` : ''}
                    </div>
                `;
                
                card.innerHTML = content;
                eventListEl.appendChild(card);
            });
            
        } else {
            eventListEl.innerHTML = '<div class="no-event">Nenhum evento para este dia.</div>';
        }
        
        document.querySelectorAll('.edit-event-btn').forEach(btn => {
            btn.onclick = (e) => {
                e.preventDefault();
                const id = btn.dataset.id;
                const eventToEdit = allEvents.find(e => e.id == id); 
                if (eventToEdit) {
                    loadFormForEdit(eventToEdit);
                }
            };
        });
    }
    
    function loadFormForEdit(event) {
        if (isDateInPast(event.dataEvento)) {
            alert("Não é possível editar eventos com data de início no passado.");
            return;
        }
        
        document.getElementById('idEvento').value = event.id;
        document.getElementById('titulo').value = event.titulo;
        document.getElementById('dataEvento').value = event.dataEvento;
        document.getElementById('dataFim').value = event.dataFim || '';
        document.getElementById('horario').value = event.horario || '';
        document.getElementById('descricao').value = event.descricao;
        document.getElementById('idCategoria').value = event.categoria.id;
        document.getElementById('tipoRepeticao').value = event.tipoRepeticao || 'NENHUM';
        
        const anexoUrlField = document.getElementById('anexoUrl');
        if (anexoUrlField) {
             anexoUrlField.value = event.anexoUrl || '';
             anexoUrlField.disabled = false;
        }
        
        if (fileNameDisplay) fileNameDisplay.textContent = '';
        if (fileInput) fileInput.value = '';
        
        toggleFormVisibility(true, true);
        
        const dataAtualInput = eventForm.querySelector('input[name="dataAtual"]');
        if (dataAtualInput) dataAtualInput.value = getSelectedDateKey();
    }
    
    function renderWeeklyView() {
        const weeklyGrid = document.getElementById('weeklyGrid');
        if (weeklyGrid) {
            weeklyGrid.innerHTML = '<p style="color:#FFF;">Funcionalidade de Visualização Semanal em desenvolvimento...</p>';
        }
    }
    
    function switchView(viewName) {
        document.querySelectorAll('.calendar-widget, .view-content').forEach(el => {
            el.classList.remove('view-active');
            el.classList.add('view-hidden');
        });
        
        document.querySelectorAll('.btn-view').forEach(btn => {
            btn.classList.remove('active');
        });
        
        currentView = viewName;
        const targetElement = document.getElementById(`view${viewName.charAt(0).toUpperCase() + viewName.slice(1)}`);
        if (targetElement) {
            targetElement.classList.add('view-active');
            targetElement.classList.remove('view-hidden');
        }
        const targetButton = document.querySelector(`.btn-view[data-view="${viewName}"]`);
        if (targetButton) {
            targetButton.classList.add('active');
        }
        
        if (viewName === 'mensal') {
             renderCalendar();
        } else if (viewName === 'semanal') {
            renderWeeklyView();
        }
    }

    if (prevYearBtn) prevYearBtn.onclick = () => { currentYear--; renderCalendar(); };
    if (nextYearBtn) nextYearBtn.onclick = () => { currentYear++; renderCalendar(); };
    
    if (monthListEl) {
        months.forEach((month, index) => {
            const li = document.createElement('li');
            li.textContent = month.substring(0, 3);
            li.onclick = () => {
                currentMonth = index;
                selectedDay = 1; 
                renderCalendar();
            };
            monthListEl.appendChild(li);
        });
    }
    
    if (addEventButton) {
        addEventButton.onclick = () => {
            const selectedDateKey = getSelectedDateKey();
            if (isDateInPast(selectedDateKey)) {
                alert("Não é possível criar eventos em datas passadas.");
                return;
            }
            resetForm();
            toggleFormVisibility(true);
            const tituloInput = document.getElementById('titulo');
            if (tituloInput) tituloInput.focus();
        };
    }

    if (cancelButton) {
        cancelButton.onclick = (e) => {
            e.preventDefault();
            resetForm();
            toggleFormVisibility(false);
        };
    }
    
    if (eventForm) {
        eventForm.onsubmit = () => {
            const dataEventoEl = document.getElementById('dataEvento');
            if (!dataEventoEl) return false;
            
            const dataEventoForm = dataEventoEl.value;
            if (isDateInPast(dataEventoForm)) {
                alert("A operação não pode ser realizada com a data de início no passado. Por favor, selecione uma data futura.");
                return false;
            }
            
            const dataAtualInput = eventForm.querySelector('input[name="dataAtual"]');
            if (dataAtualInput) {
                dataAtualInput.value = getSelectedDateKey();
            }
            return true;
        }
    }
    
    
    if (fileLabel && fileInput) {
        fileLabel.onclick = () => {
            fileInput.click();
        };
    }
    
    if (fileInput) {
        fileInput.onchange = () => {
            if (fileInput.files.length > 0) {
                if (fileNameDisplay) fileNameDisplay.textContent = `Arquivo selecionado: ${fileInput.files[0].name}`;
                if (anexoUrlInput) {
                    anexoUrlInput.disabled = true;
                    anexoUrlInput.value = '';
                }
            } else {
                if (fileNameDisplay) fileNameDisplay.textContent = '';
                if (anexoUrlInput) anexoUrlInput.disabled = false;
            }
        };
    }
    
    if (anexoUrlInput) {
        anexoUrlInput.oninput = () => {
            if (anexoUrlInput.value) {
                if (fileInput) fileInput.value = '';
                if (fileNameDisplay) fileNameDisplay.textContent = '';
            }
        };
    }
    
    
    if (viewSwitcher) {
        viewSwitcher.querySelectorAll('.btn-view').forEach(btn => {
            btn.onclick = () => switchView(btn.dataset.view);
        });
        
        const urlParams = new URLSearchParams(window.location.search);
        const initialView = urlParams.get('view') || 'mensal';
        switchView(initialView);
    } else {
        renderCalendar();
    }
    
    document.querySelectorAll('.edit-event-list').forEach(btn => {
        btn.onclick = (e) => {
            e.preventDefault();
            const id = btn.dataset.id;
            const eventToEdit = allEvents.find(e => e.id == id);
            if (eventToEdit) {
                loadFormForEdit(eventToEdit);
                switchView('mensal');
            }
        };
    });
});