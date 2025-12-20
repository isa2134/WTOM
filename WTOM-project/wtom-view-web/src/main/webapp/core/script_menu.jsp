<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const toggle = document.getElementById('sidebar-toggle');

    if (sidebar && toggle) {
        const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
        if (isCollapsed) {
            sidebar.classList.add('collapsed');
        }

        toggle.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            
            const newState = sidebar.classList.contains('collapsed');
            localStorage.setItem('sidebarCollapsed', newState);
        });
    }
});
</script>