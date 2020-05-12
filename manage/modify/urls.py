from django.urls import path
from .views import HomePageView,export_module, export_teacher, export_student, import_module,import_teacher,import_student,import_selected

urlpatterns=[
    path('',HomePageView.as_view(),name='home'),
    path('export_module/',export_module,name='export_module'),
    path('export_teacher/',export_teacher,name='export_teacher'),
    path('export_student_by_modulecode/',export_student,name='export_student'),
    path('import_teacher/',import_teacher,name='import_teacher'),
    path('import_student/',import_student,name='import_student'),
    path('import_module/',import_module,name='import_module'),
    path('import_selected/',import_selected,name='import_selected')
]