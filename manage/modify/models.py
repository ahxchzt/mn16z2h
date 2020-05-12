from django.db import models

class Admin(models.Model):
    id = models.IntegerField(primary_key=True)
    email = models.CharField(max_length=255, blank=True, null=True)
    password = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'admin'


class AuthGroup(models.Model):
    name = models.CharField(unique=True, max_length=150)

    class Meta:
        managed = False
        db_table = 'auth_group'


class AuthGroupPermissions(models.Model):
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)
    permission = models.ForeignKey('AuthPermission', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_group_permissions'
        unique_together = (('group', 'permission'),)


class AuthPermission(models.Model):
    name = models.CharField(max_length=255)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING)
    codename = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'auth_permission'
        unique_together = (('content_type', 'codename'),)


class AuthUser(models.Model):
    password = models.CharField(max_length=128)
    last_login = models.DateTimeField(blank=True, null=True)
    is_superuser = models.IntegerField()
    username = models.CharField(unique=True, max_length=150)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=150)
    email = models.CharField(max_length=254)
    is_staff = models.IntegerField()
    is_active = models.IntegerField()
    date_joined = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'auth_user'


class AuthUserGroups(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_groups'
        unique_together = (('user', 'group'),)


class AuthUserUserPermissions(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    permission = models.ForeignKey(AuthPermission, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_user_permissions'
        unique_together = (('user', 'permission'),)


class DjangoAdminLog(models.Model):
    action_time = models.DateTimeField()
    object_id = models.TextField(blank=True, null=True)
    object_repr = models.CharField(max_length=200)
    action_flag = models.PositiveSmallIntegerField()
    change_message = models.TextField()
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'django_admin_log'


class DjangoContentType(models.Model):
    app_label = models.CharField(max_length=100)
    model = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'django_content_type'
        unique_together = (('app_label', 'model'),)


class DjangoMigrations(models.Model):
    app = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    applied = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_migrations'


class DjangoSession(models.Model):
    session_key = models.CharField(primary_key=True, max_length=40)
    session_data = models.TextField()
    expire_date = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_session'


class Module(models.Model):
    module_code = models.CharField(primary_key=True, max_length=50)
    module_name = models.CharField(max_length=255, blank=True, null=True)
    time = models.CharField(max_length=30, blank=True, null=True)
    data = models.CharField(max_length=30, blank=True, null=True)
    teacher = models.ForeignKey('Teacher', models.DO_NOTHING, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'module'
    def __str__(self):
        return '%s - (%s)'%(self.module_name,self.module_code)


class Student(models.Model):
    absent_time = models.IntegerField(blank=True, null=True)
    name = models.CharField(max_length=40, blank=True, null=True)
    student_id = models.CharField(primary_key=True, max_length=40)
    email = models.CharField(max_length=40, blank=True, null=True)
    assistant_email = models.CharField(max_length=40, blank=True, null=True)
    blueteeth_address = models.CharField(max_length=40, blank=True, null=True)
    major=models.CharField(max_length=255)
    gender=models.CharField(max_length=255,choices=(('male','male'),('female','female')))
    grade=models.CharField(max_length=255,choices=(('1','grade 1'),('2','grade 2'),('3','grade 3'),('4','grade 4')))
    sclass=models.CharField(max_length=255)
    class Meta:
        managed = False
        db_table = 'student'
    def __str__(self):
        return '%s - (%s)'%(self.name,self.student_id)


class StudentModule(models.Model):
    student = models.ForeignKey(Student, models.DO_NOTHING, blank=True, null=True)
    module_code = models.ForeignKey(Module, models.DO_NOTHING, db_column='module_code', blank=True, null=True)
    absent_time=models.IntegerField(default=0,blank=True)
    class Meta:
        managed = False
        db_table = 'student_module'
    def __str__(self):
        return '%s(%s) - %s(%s)'%(self.student.name,self.student.student_id,self.module_code.module_name,self.module_code.module_code)


class Teacher(models.Model):
    teacher_id = models.CharField(primary_key=True, max_length=40)
    email = models.CharField(max_length=40)
    password = models.CharField(max_length=40)
    name=models.CharField(max_length=255)
    gender=models.CharField(max_length=255,choices=(('male','male'),('female','female')))
    institute=models.CharField(max_length=255)

    class Meta:
        managed = False
        db_table = 'teacher'
    def __str__(self):
        return '%s - (%s)'%(self.name,self.teacher_id)



