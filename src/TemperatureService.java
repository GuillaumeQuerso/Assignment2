import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class TemperatureService {
	private String LDR_PATH = "/sys/bus/iio/devices/iio:device0/in_voltage";
	
	
	private int readAnalog(int number){
		String fileName = LDR_PATH+number+"_raw";
		try{
			BufferedReader buff = new BufferedReader(new FileReader(fileName));
			number = Integer.parseInt(buff.readLine());
			buff.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return number;
	}
	
	private float getTemperature(int adc_value){
		float cur_voltage = adc_value * (1.80f/4096.0f);
		float diff_degreesC = (cur_voltage-0.75f)/0.01f;
		return (25.0f + diff_degreesC);
	}
	
	public float setTemperature(int number){
		int adc_value = this.readAnalog(number);
		return this.getTemperature(adc_value);
	}
}
