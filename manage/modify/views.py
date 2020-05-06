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


# 导出Excel文件
def export_excel(request):
    lista=[]
    module_code = request.GET.get("module_code")
    list_obj=Student.objects.filter(studentmodule__module_code=module_code).values('absent_time',
                                                                                   'name',
                                                                                   'student_id',
                                                                                   'email')
    # 设置HTTPResponse的类型
    response = HttpResponse(content_type='application/vnd.ms-excel')
    response['Content-Disposition'] = 'attachment;filename='+str(module_code)+'.xls'
    """导出excel表"""
    if list_obj:
        # 创建工作簿
        ws = xlwt.Workbook(encoding='utf-8')
        # 添加第一页数据表
        w = ws.add_sheet('sheet1')  # 新建sheet（sheet的名称为"sheet1"）
        # 写入表头
        w.write(0, 0, u'name')
        w.write(0, 1, u'student_id')
        w.write(0, 2, u'absent_time')
        w.write(0, 3, u'email')
        # 写入数据
        excel_row = 1
        for obj in list_obj:
            name = obj['name']
            sum = obj['student_id']
            lng = obj['absent_time']
            lat = obj['email']
            # 写入每一行对应的数据
            w.write(excel_row, 0, name)
            w.write(excel_row, 1, sum)
            w.write(excel_row, 2, lng)
            w.write(excel_row, 3, lat)
            excel_row += 1
        # 写出到IO
        output = BytesIO()
        ws.save(output)
        # 重新定位到开始
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
                Teacher.objects.create(
                    teacher_id=row_values[0],
                    name=row_values[1],
                    email=row_values[2],
                    password=row_values[3],
                )
            return HttpResponse('success')

@login_required
@csrf_exempt
def import_student(request):
    # Get the file uploaded by the front end
    excel_file = request.FILES.get('excel_file', '')
    file_type = excel_file.name.split('.')[1]
    if file_type in ['xlsx', 'xls']:  # 支持这两种文件格式
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


                        Student.objects.create(
                            absent_time=0,
                            name=row_values[0],
                            student_id=str(int(row_values[1])),
                            email=str(row_values[2]),
                            assistant_email=str(row_values[3]),
                            blueteeth_address=str(row_values[4])
                        )
            except:
                return HttpResponse('解析excel文件或者数据插入错误！')
        return HttpResponse("success")
    else:
        return HttpResponse("wrong file type")
@login_required
@csrf_exempt
def import_module(request):
    # Get the file uploaded by the front end
    excel_file = request.FILES.get('excel_file', '')
    file_type = excel_file.name.split('.')[1]
    if file_type in ['xlsx', 'xls']:  # 支持这两种文件格式
        # 打开工作文件
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



