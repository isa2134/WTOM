<%@page contentType="text/html" pageEncoding="UTF-8"%>
        <script src="${pageContext.request.contextPath}/js/cssControl.js"></script>
        <body>
            <footer class="site-footer">
    <div class="footer-wrapper">
        
        <div class="footer-top">
            
            <div class="footer-intro">
                <h2>TOM: Tecnologia, Olimpíadas e Matemática</h2>
                <p>
                    A TOM é uma plataforma dedicada a preparar estudantes para olimpíadas de matemática, fornecendo conteúdo de alta qualidade, desafios e uma comunidade ativa.
                </p>
            </div>

            <div class="footer-column">
                <h3>Conecte-se Conosco</h3>
                <ul>
                    <li><a href="https://www.gtmat.cefetmg.br/treinamento-olimpico-matematico-cefet-mg-de-belo-horizonte/"><i class="fa-solid fa-envelope"></i> Fale Conosco</a></li>
                    <li><a href="https://chat.whatsapp.com/GboYSSHgb49H5vJRwD6FIe?utm_source=ig&utm_medium=social&utm_content=link_in_bio&fbclid=PAZXh0bgNhZW0CMTEAc3J0YwZhcHBfaWQMMjU2MjgxMDQwNTU4AAGnbAXdynSa8-_vOZgG--5N_4iQgzrh9rFoqglNHJ0Ph1lq834PfpTOHJByn4I_aem_n4v3GbE8j7BUdgHASwgYfg"><i class="fa-brands fa-whatsapp"></i> Canal de Avisos</a></li>
                    <li><a href="https://www.instagram.com/equipetom/"><i class="fa-brands fa-instagram"></i> Nosso Instagram</a></li>
                    <li><a href="${pageContext.request.contextPath}/DuvidaController?acao=listar"><i class="fa-solid fa-question-circle"></i> Suporte ao Aluno</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3>Sobre a Plataforma</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/footerPlaceholder/nossaMissao.jsp">Nossa Missão</a></li>
                        <li><a href="${pageContext.request.contextPath}/footerPlaceholder/carreiras.jsp">Carreiras</a></li>
                        <li><a href="${pageContext.request.contextPath}/footerPlaceholder/termosDeUso.jsp">Termos de Uso</a></li>
                        <li><a href="${pageContext.request.contextPath}/footerPlaceholder/politicaDePrivacidade.jsp">Política de Privacidade</a></li>
                        <li><a href="${pageContext.request.contextPath}/footerPlaceholder/mapaDoSite.jsp">Mapa do Site</a></li>
                    </ul>
            </div>

            <div class="footer-column">
                <h3>Recursos</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/olimpiada">Olimpíadas Atuais</a></li>
                    <li><a href="${pageContext.request.contextPath}/core/ranking/listar.jsp">Ranking Geral</a></li>
                    <li><a href="${pageContext.request.contextPath}/DuvidaController?acao=listar">Fórum de Dúvidas</a></li>
                    <li><a href="${pageContext.request.contextPath}/ConteudoController?acao=listarTodos">Biblioteca de Conteúdos</a></li>
                </ul>
            </div>
            
        </div>
        
        <div class="footer-bottom">
            <p>&copy; <%= java.time.Year.now() %> TOM. Todos os direitos reservados.</p>
            <div class="footer-legal-links">
                <a href="${pageContext.request.contextPath}/footerPlaceholder/termosDeUso.jsp">Termos de Uso</a> |
                <a href="${pageContext.request.contextPath}/footerPlaceholder/politicaDePrivacidade.jsp">Política de Privacidade</a> |
                <a href="${pageContext.request.contextPath}/footerPlaceholder/disclaimerLegal.jsp">Disclaimer Legal</a>
            </div>
        </div>
        
    </div>
</footer>
        </body>
</html>
