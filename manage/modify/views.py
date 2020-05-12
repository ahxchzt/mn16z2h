import xlwt
import xlrd
from django.db import transaction
from django.http import HttpResponse
from .models import Student,Teacher,Module,StudentModule
from io import BytesIO
from django.views.generic import TemplateView
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.decorators import login_required
class HomePageView(TemplateView):
    template_name = 'test.html'
@login_required
def export_module(request):
    list_obj=Module.objects.all().values('module_code',
                                          'module_name',
                                          'time',
                                          'data',
                                          'teacher__teacher_id',
                                         'teacher__name')
    # set the type of HTTPResponse
    response = HttpResponse(content_type='application/vnd.ms-excel')
    response['Content-Disposition'] = 'attachment;filename='+'Modules'+'.xls'
    if list_obj:
        # Create workbook
        ws = xlwt.Workbook(encoding='utf-8')
        # add sheet
        w = ws.add_sheet('sheet1')
        # Write header
        w.write(0, 0, u'module_code')
        w.write(0, 1, u'module_name')
        w.write(0, 2, u'time')
        w.write(0, 3, u'data')
        w.write(0, 4, u'teacher_id')
        w.write(0, 5, u'teacher_name')
        # Write data
        excel_row = 1
        for obj in list_obj:
            module_code = obj['module_code']
            module_name = obj['module_name']
            time = obj['time']
            data=obj['data']
            teacher_id=obj['teacher__teacher_id']
            teacher_name=obj['teacher__name']
            w.write(excel_row, 0, module_code)
            w.write(excel_row, 1, module_name)
            w.write(excel_row, 2, time)
            w.write(excel_row, 3, data)
            w.write(excel_row, 4, teacher_id)
            w.write(excel_row, 5, teacher_name)
            excel_row += 1
        output = BytesIO()
        ws.save(output)
        # Relocate to the beginning of the file
        output.seek(0)
        response.write(output.getvalue())
    return response
@login_required
def export_teacher(request):
    list_obj=Teacher.objects.all().values('teacher_id',
                                          'name',
                                          'email',
                                          'password',
                                          'gender',
                                          'institute')
    # set the type of HTTPResponse
    response = HttpResponse(content_type='application/vnd.ms-excel')
    response['Content-Disposition'] = 'attachment;filename='+'Teachers'+'.xls'
    if list_obj:
        # create workbook
        ws = xlwt.Workbook(encoding='utf-8')
        # add sheet
        w = ws.add_sheet('sheet1')
        # write header
        w.write(0, 0, u'teacher_id')
        w.write(0, 1, u'name')
        w.write(0, 2, u'email')
        w.write(0, 3, u'password')
        w.write(0, 4, u'gender')
        w.write(0, 5, u'institute')
        # write data
        excel_row = 1
        for obj in list_obj:
            name = obj['name']
            id = obj['teacher_id']
            email = obj['email']
            gender=obj['gender']
            institute=obj['institute']
            password=obj['password']
            w.write(excel_row, 0, id)
            w.write(excel_row, 1, name)
            w.write(excel_row, 2, email)
            w.write(excel_row, 3, password)
            w.write(excel_row, 4, gender)
            w.write(excel_row, 5, institute)
            excel_row += 1
        output = BytesIO()
        ws.save(output)
        # Relocate to the beginning of the file
        output.seek(0)
        response.write(output.getvalue())
    return response
def export_student(request):
    module_code = request.GET.get("module_code")
    list_obj=Student.objects.filter(studentmodule__module_code=module_code).values('absent_time',
                                                                                   'name',
                                                                                   'student_id',
                                                                                   'email',
                                                                                   'assistant_email',
                                                                                   'gender',
                                                                                   'major',
                                                                                   'grade',
                                                                                   'sclass')
    # set the type of HTTPResponse
    response = HttpResponse(content_type='application/vnd.ms-excel')
    response['Content-Disposition'] = 'attachment;filename='+str(module_code)+'.xls'
    if list_obj:
        # create workbook
        ws = xlwt.Workbook(encoding='utf-8')
        # add sheet
        w = ws.add_sheet('sheet1')
        # write header
        w.write(0, 0, u'name')
        w.write(0, 1, u'student_id')
        w.write(0, 2, u'absent_time')
        w.write(0, 3, u'email')
        w.write(0, 4, u'assistant_email')
        w.write(0, 5, u'gender')
        w.write(0, 6, u'major')
        w.write(0, 7, u'grade')
        w.write(0, 8, u'class')
        # write data
        excel_row = 1
        for obj in list_obj:
            name = obj['name']
            id = obj['student_id']
            absent_time = obj['absent_time']
            email = obj['email']
            assistant_email=obj['assistant_email']
            gender=obj['gender']
            major=obj['major']
            grade=obj['grade']
            sclass=obj['sclass']
            w.write(excel_row, 0, name)
            w.write(excel_row, 1, id)
            w.write(excel_row, 2, absent_time)
            w.write(excel_row, 3, email)
            w.write(excel_row, 4, assistant_email)
            w.write(excel_row, 5, gender)
            w.write(excel_row, 6, major)
            w.write(excel_row, 7, grade)
            w.write(excel_row, 8, sclass)
            excel_row += 1
        output = BytesIO()
        ws.save(output)
        # Relocate to the beginning of the file
        output.seek(0)
        response.write(output.getvalue())
    return response

# Only administrators can import excel files
@login_required
@csrf_exempt
def import_teacher(request):
    if request.method=='POST':
        # Get the file uploaded by the front end
        excel_file = request.FILES.get('excel_file')
        data = xlrd.open_workbook(filename=None, file_contents=excel_file.read())
        tables = data.sheets()
        # Get data in each sheet
        for table in tables:
            rows = table.nrows
        with transaction.atomic():
            # Get data of each row and write it to the designed database table
            # Starting from 1 is to ignore the header
            for row in range(1, rows):
                row_values = table.row_values(row)
                if type(row_values[0]).__name__ == 'float':
                    row_values[0] = str(int(row_values[0]))
                if type(row_values[3]).__name__ == 'float':
                    row_values[3] = str(int(row_values[3]))
                Teacher.objects.create(
                    teacher_id=row_values[0],
                    name=row_values[1],
                    email=row_values[2],
                    password=row_values[3],
                    gender=row_values[4],
                    institute=row_values[5]
                )
            return HttpResponse('success')

@login_required
@csrf_exempt
def import_student(request):
    # Get the file uploaded by the front end
    excel_file = request.FILES.get('excel_file', '')
    file_type = excel_file.name.split('.')[1]
    if file_type in ['xlsx', 'xls']:
        data = xlrd.open_workbook(filename=None, file_contents=excel_file.read())
        tables = data.sheets()
        # Get data in each sheet
        for table in tables:
            rows = table.nrows
            try:
                with transaction.atomic():
                    # Get data of each row and write it to the designed database table
                    # Starting from 1 is to ignore the header
                    for row in range(1, rows):
                        row_values = table.row_values(row)
                        if type(row_values[1]).__name__ == 'float':
                            row_values[1] = str(int(row_values[1]))
                        if type(row_values[6]).__name__ == 'float':
                            row_values[6] = str(int(row_values[6]))
                        if type(row_values[7]).__name__ == 'float':
                            row_values[7] = str(int(row_values[7]))

                        Student.objects.create(
                            absent_time=0,
                            name=row_values[0],
                            student_id=row_values[1],
                            email=row_values[2],
                            assistant_email=row_values[3],
                            gender=row_values[4],
                            major=row_values[5],
                            grade=row_values[6],
                            sclass=row_values[7],
                            blueteeth_address=row_values[8]


                        )
            except:
                return HttpResponse('Parsing excel file or data insertion error!')
        return HttpResponse("success")
    else:
        return HttpResponse("wrong file type")
@login_required
@csrf_exempt
def import_module(request):
    # Get the file uploaded by the front end
    excel_file = request.FILES.get('excel_file', '')
    file_type = excel_file.name.split('.')[1]
    if file_type in ['xlsx', 'xls']:
        data = xlrd.open_workbook(filename=None, file_contents=excel_file.read())
        tables = data.sheets()
        # Get data in each sheet
        for table in tables:
            rows = table.nrows
            try:
                with transaction.atomic():
                    # Get data of each row and write it to the designed database table
                    # Starting from 1 is to ignore the header
                    for row in range(1, rows):
                        row_values = table.row_values(row)
                        if type(row_values[4]).__name__=='float':
                            row_values[4]=int(row_values[4])
                        t=Teacher.objects.get(teacher_id=str(row_values[4]))
                        Module.objects.create(
                            module_code=str(row_values[0]),
                            module_name=str(row_values[1]),
                            time=str(row_values[2]),
                            data=str(row_values[3]),
                            teacher=t
                        )
            except:
                return HttpResponse
        return HttpResponse("success")
    else:
        return HttpResponse("wrong file type")

@login_required
@csrf_exempt
def import_selected(request):
    # Get the file uploaded by the front end
    excel_file = request.FILES.get('excel_file', '')
    file_type = excel_file.name.split('.')[1]
    if file_type in ['xlsx', 'xls']:
        # open workbook
        data = xlrd.open_workbook(filename=None, file_contents=excel_file.read())
        tables = data.sheets()
        # Get data in each sheet
        for table in tables:
            rows = table.nrows
            try:
                with transaction.atomic():
                    # Get data of each row and write it to the designed database table
                    # Starting from 1 is to ignore the header
                    for row in range(1, rows):
                        row_values = table.row_values(row)
                        if type(row_values[0]).__name__=='float':
                            row_values[0]=int(row_values[0])
                        if type(row_values[1]).__name__ == 'float':
                            row_values[1] = int(row_values[1])
                        m=Module.objects.get(module_code=str(row_values[0]))
                        s=Student.objects.get(student_id=str(row_values[1]))
                        StudentModule.objects.create(
                            module_code=m,
                            student=s
                        )
            except:
                return HttpResponse
        return HttpResponse("success")
    else:
        return HttpResponse("wrong file type")



