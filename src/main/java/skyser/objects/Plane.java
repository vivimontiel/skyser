package skyser.objects;

public class Plane{

    private String id, id_pilot, atc_number,model, path_picture;

    public Plane(String id,String id_p, String Atc, String m, String path_picture){
        this.id = id;
        this.id_pilot = id_p;
        this.atc_number = Atc;
        this.model = m;
        this.path_picture =path_picture;
    }

    public Plane(){}

    public String getid_pilot(){
        return id_pilot;
    }
    public String getatc_number(){
        return atc_number;
    }
    public void setatc_number(String atc){
        this.atc_number = atc;
    }
    public String getModel(){
        return model;
    }
    public void setModel(String m){
        this.model = m;
    }
    public String getpath_picture(){
        return path_picture;
    }
    public void setpath_picture(String img){
        this.path_picture = img;
    }

    public String getId() {
        return id;
    }
}