package skyser.objects;

public class ComposedFlight {

    private Flight f1;
    private Flight f2;


    public ComposedFlight(Flight f1,Flight f2){
        this.f1=f1;
        this.f2=f2;
    }

    public ComposedFlight(){}

    public Flight getFlightf1(){
        return f1;
    }

    public Flight getFlightf2(){
        return f2;
    }
}
