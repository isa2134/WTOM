const months = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];

const INITIAL_DATE_KEY = document.getElementById('initialDateInput') ? document.getElementById('initialDateInput').value : '';

let date = new Date();
let currentYear = date.getFullYear();
let currentMonth = date.getMonth();
let selectedDay = date.getDate();

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
        console.error("Erro ao processar data inicial:", e);
    }
}

const monthListEl = document.getElementById('monthList');
const daysGridEl = document.getElementById('daysGrid');
const yearDisplayEl = document.getElementById('currentYear');
const monthHeaderEl = document.getElementById('monthHeader');
const selectedDateDisplayEl = document.getElementById('selectedDateDisplay');
const eventListEl = document.getElementById('eventList');
const eventDataInput = document.getElementById('eventDataInput');
const eventForm = document.getElementById('eventForm');
const dataEventoInput = document.getElementById('dataEvento');
const addEventButton = document.getElementById('addEventButton');
const formContainer = document.getElementById('formContainer');

const TODAY_DATE = document.getElementById('todayDateInput') ? document.getElementById('todayDateInput').value : '';
const TODAY = new Date(TODAY_DATE + 'T00:00:00');

const acaoInput = document.getElementById('acaoInput');
const idEventoInput = document.getElementById('idEventoInput');
const tituloInput = document.getElementById('titulo');
const descricaoInput = document.getElementById('descricao');

const dataFimInput = document.getElementById('dataFim');
const horarioInput = document.getElementById('horario');
const idCategoriaInput = document.getElementById('idCategoria');

const events = eventDataInput ? JSON.parse(eventDataInput.value) : {};

function formatKey(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getSelectedDateKey() {
    const monthPadded = (currentMonth + 1).toString().padStart(2, '0');
    const dayPadded = selectedDay.toString().padStart(2, '0');
    return `${currentYear}-${monthPadded}-${dayPadded}`;
}

function isDateInPast(dateKey) {
    const selectedDate = new Date(dateKey + 'T00:00:00');
    return selectedDate.getTime() < TODAY.getTime();
}

function getAllEvents() {
    const allEvents = [];
    for (const key in events) {
        if (events.hasOwnProperty(key)) {
            allEvents.push(...events[key]);
        }
    }
    return allEvents;
}

function getAllEventsForDay(dateKey) {
    const targetDate = new Date(dateKey + 'T00:00:00');
    const allEvents = getAllEvents();

    return allEvents.filter(evt => {
        const startDate = new Date(evt.dataEvento + 'T00:00:00');
        let endDate = startDate;
        if (evt.dataFim) {
            endDate = new Date(evt.dataFim + 'T00:00:00');
        }

        return targetDate.getTime() >= startDate.getTime() && targetDate.getTime() <= endDate.getTime();
    }).sort((a, b) => {
        const timeA = a.horario || '23:59';
        const timeB = b.horario || '23:59';
        return timeA.localeCompare(timeB) || a.titulo.localeCompare(b.titulo);
    });
}

function toggleFormVisibility(show) {
    if (formContainer) {
        if (show) {
            formContainer.classList.remove('hidden');
        } else {
            formContainer.classList.add('hidden');
        }
    }
    if (addEventButton) {
        const selectedDateKey = getSelectedDateKey();
        const isPast = isDateInPast(selectedDateKey);

        if (window.canManage) {
            if (show) {
                addEventButton.style.display = 'none';
            } else if (!isPast) {
                addEventButton.style.display = 'inline-block';
            } else {
                addEventButton.style.display = 'none';
            }
        } else {
            addEventButton.style.display = 'none';
        }
    }
}

function resetForm() {
    toggleFormVisibility(false);
    if (idEventoInput) {
        idEventoInput.value = '';
    }
    if (acaoInput) {
        acaoInput.value = 'cadastrar';
    }
    if (tituloInput) {
        tituloInput.value = '';
    }
    if (descricaoInput) {
        descricaoInput.value = '';
    }
    if (dataFimInput) {
        dataFimInput.value = '';
    }
    if (horarioInput) {
        horarioInput.value = '';
    }
    if (idCategoriaInput) {
        idCategoriaInput.value = '';
    }
    const submitButton = document.querySelector('#eventForm button[type="submit"]');
    if (submitButton) {
        submitButton.textContent = 'Salvar Evento';
    }
}

function editEvent(evt) {
    if (!window.canManage)
        return;

    const selectedDateKey = getSelectedDateKey();
    if (isDateInPast(selectedDateKey)) {
        alert("Não é possível editar eventos em datas passadas.");
        return;
    }

    if (idEventoInput) {
        idEventoInput.value = evt.id;
    }
    if (acaoInput) {
        acaoInput.value = 'editar';
    }
    if (tituloInput) {
        tituloInput.value = evt.titulo;
    }
    if (descricaoInput) {
        descricaoInput.value = evt.descricao.replace(/\\n/g, '\n');
    }
    if (dataEventoInput) {
        dataEventoInput.value = evt.dataEvento;
    }
    if (dataFimInput) {
        dataFimInput.value = evt.dataFim || '';
    }
    if (horarioInput) {
        horarioInput.value = evt.horario || '';
    }
    if (idCategoriaInput) {
        idCategoriaInput.value = evt.idCategoria;
    }

    const submitButton = document.querySelector('#eventForm button[type="submit"]');
    if (submitButton) {
        submitButton.textContent = 'Atualizar Evento';
    }

    toggleFormVisibility(true);

    if (tituloInput) {
        tituloInput.focus();
    }
}

function renderCalendar() {

    yearDisplayEl.innerText = currentYear;
    monthHeaderEl.innerText = `${months[currentMonth].toUpperCase()} ${currentYear}`;

    monthListEl.innerHTML = "";
    months.forEach((m, index) => {
        const li = document.createElement('li');
        li.innerText = m;
        if (index === currentMonth)
            li.classList.add('active');
        li.onclick = () => {
            currentMonth = index;
            renderCalendar();
        };
        monthListEl.appendChild(li);
    });

    daysGridEl.innerHTML = "";
    const firstDayIndex = new Date(currentYear, currentMonth, 1).getDay();
    const lastDay = new Date(currentYear, currentMonth + 1, 0).getDate();

    for (let i = 0; i < firstDayIndex; i++) {
        const emptyDiv = document.createElement('div');
        emptyDiv.classList.add('day-cell', 'empty');
        daysGridEl.appendChild(emptyDiv);
    }

    for (let i = 1; i <= lastDay; i++) {
        const dayDiv = document.createElement('div');
        dayDiv.classList.add('day-cell');
        dayDiv.innerText = i;

        const dateKey = formatKey(new Date(currentYear, currentMonth, i));

        const dayEvents = getAllEventsForDay(dateKey);

        if (dayEvents.length > 0) {
            dayDiv.classList.add('has-event');
            const dot = document.createElement('div');
            dot.classList.add('dot');

            const firstEventColor = dayEvents[0].corHex || 'var(--cal-event-dot)';
            dot.style.backgroundColor = firstEventColor;

            dayDiv.appendChild(dot);
        }

        if (dateKey === TODAY_DATE) {
            dayDiv.classList.add('today');
        }

        if (i === selectedDay && new Date(currentYear, currentMonth, i).toDateString() === new Date(currentYear, currentMonth, selectedDay).toDateString()) {
            dayDiv.classList.add('selected');
        }


        dayDiv.onclick = () => {
            document.querySelectorAll('.day-cell').forEach(el => el.classList.remove('selected'));
            dayDiv.classList.add('selected');
            selectedDay = i;
            updateEventPanel(i);
        };

        daysGridEl.appendChild(dayDiv);
    }

    updateEventPanel(selectedDay);
}

document.getElementById('prevYear').onclick = () => {
    currentYear--;
    renderCalendar();
};

document.getElementById('nextYear').onclick = () => {
    currentYear++;
    renderCalendar();
};

function updateEventPanel(day) {
    resetForm();

    const dayPadded = day.toString().padStart(2, '0');
    const monthPadded = (currentMonth + 1).toString().padStart(2, '0');
    const fullDate = `${dayPadded}/${monthPadded}/${currentYear}`;
    selectedDateDisplayEl.innerText = fullDate;

    const dateKey = `${currentYear}-${monthPadded}-${dayPadded}`;
    eventListEl.innerHTML = "";

    if (dataEventoInput) {
        dataEventoInput.value = dateKey;
    }

    const isPast = isDateInPast(dateKey);
    if (addEventButton) {
        if (window.canManage) {
            addEventButton.style.display = isPast ? 'none' : 'inline-block';
        } else {
            addEventButton.style.display = 'none';
        }
    }


    const dayEvents = getAllEventsForDay(dateKey);

    if (dayEvents.length > 0) {
        dayEvents.forEach(evt => {
            const card = document.createElement('div');
            card.classList.add('event-card');

            const eventColor = evt.corHex || 'var(--cal-event-dot)';
            card.style.borderLeft = `3px solid ${eventColor}`;

            const eventHeader = document.createElement('div');
            eventHeader.classList.add('event-header');

            const eventTitleDiv = document.createElement('div');
            eventTitleDiv.classList.add('event-title');
            eventTitleDiv.innerHTML = `<span class="event-marker" style="background-color: ${eventColor};"></span> ${evt.titulo}`;
            eventHeader.appendChild(eventTitleDiv);

            const eventDetailsMeta = document.createElement('div');
            eventDetailsMeta.classList.add('event-details-meta');

            if (evt.horario) {
                const timeSpan = document.createElement('span');
                timeSpan.classList.add('event-time');
                timeSpan.innerText = `⏱️ ${evt.horario}`;
                eventDetailsMeta.appendChild(timeSpan);
            }

            if (evt.dataFim && evt.dataFim !== evt.dataEvento) {
                const [y, m, d] = evt.dataFim.split('-');
                const durationSpan = document.createElement('span');
                durationSpan.classList.add('event-duration');
                durationSpan.innerText = `📅 Fim: ${d}/${m}/${y}`;
                eventDetailsMeta.appendChild(durationSpan);
            }

            if (eventDetailsMeta.children.length > 0) {
                eventHeader.appendChild(eventDetailsMeta);
            }

            const eventDescDiv = document.createElement('div');
            eventDescDiv.classList.add('event-desc');
            eventDescDiv.innerText = evt.descricao;

            card.appendChild(eventHeader);
            card.appendChild(eventDescDiv);

            if (window.canManage) {
                const actionsDiv = document.createElement('div');
                actionsDiv.classList.add('event-actions');

                if (!isDateInPast(evt.dataEvento)) {
                    const editButton = document.createElement('a');
                    editButton.href = "#";
                    editButton.classList.add('edit-event-btn');
                    editButton.title = "Editar Evento";
                    editButton.innerHTML = '<i class="fa-solid fa-pen-to-square"></i>';
                    editButton.onclick = (e) => {
                        e.preventDefault();
                        editEvent(evt);
                    };
                    actionsDiv.appendChild(editButton);

                    const deleteLink = document.createElement('a');
                    deleteLink.href = `${evt.contextPath}/CronogramaController?acao=excluir&id=${evt.id}&dataAtual=${dateKey}`;
                    deleteLink.classList.add('delete-event-btn');
                    deleteLink.title = "Excluir Evento";
                    deleteLink.innerHTML = '<i class="fa-solid fa-trash-can"></i>';
                    deleteLink.onclick = () => {
                        return confirm(`Tem certeza que deseja excluir o evento: ${evt.titulo.replace(/'/g, "\\'") }?`);
                    };
                    actionsDiv.appendChild(deleteLink);
                }

                eventTitleDiv.appendChild(actionsDiv);
            }

            eventListEl.appendChild(card);
        });
    } else {
        eventListEl.innerHTML = '<div class="no-event">Nenhum evento para este dia.</div>';
    }
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
        document.getElementById('titulo').focus();
    };

    document.getElementById('cancelButton').onclick = (e) => {
        e.preventDefault();
        resetForm();
    };

    eventForm.onsubmit = () => {
        const selectedDateKey = getSelectedDateKey();

        if (isDateInPast(selectedDateKey)) {
            alert("A operação não pode ser realizada em datas passadas.");
            return false;
        }

        eventForm.querySelector('input[name="dataAtual"]').value = selectedDateKey;
        return true;
    }
}

renderCalendar();