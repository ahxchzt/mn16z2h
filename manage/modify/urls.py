from django.urls import path
from .views import HomePageView, export_excel, import_module,import_teacher,import_student

urlpatterns=[
    path('',HomePageView.as_view(),name='home'),
    path('export_excel/',export_excel,name='export_excel'),
    path('import_teacher/',import_teacher,name='import_teacher'),
    path('import_student/',import_student,name='import_student'),
    path('import_module/',import_module,name='import_module')
]