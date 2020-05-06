from django.contrib import admin
from .models import Teacher,Student,Module,StudentModule
# Register your models here.
admin.site.register(Teacher)
admin.site.register(Student)
admin.site.register(Module)
admin.site.register(StudentModule)