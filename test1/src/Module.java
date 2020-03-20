public class Module {
    private String Module_code;

    private String Module_name;

    private String data;

    private String time;
    public String getModule_code() {
        return Module_code;
    }

    public String getModule_name() {
        return Module_name;
    }

    public String getData(){
        return data;
    }
    public String getTime(){
        return time;
    }
    public void setModule_code(String Module_code){
        this.Module_code=Module_code;
    }
    public void setModule_name(String Module_name){
        this.Module_name=Module_name;
    }
    public void setData(String data){
        this.data=data;
    }
    public void setTime(String time){
        this.time=time;
    }
}
