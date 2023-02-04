
package propiedades;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class config {
    
    private String url;
    private String user;
    private String password;
    

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"C:\\DevSoftware\\Sistema\\config.properties");
		props.load(file);
		return props;

	}
     
       public void lerarquivo() throws IOException{
         
		Properties prop = getProp();

		setUrl(prop.getProperty("prop.server.url"));
		setUser(prop.getProperty("prop.server.user"));
		setPassword(prop.getProperty("prop.server.password"));

       }
       
       public void setArquivo(String url, String user, String password) throws IOException{
           
                //Cria um objeto da classe java.util.Properties
                Properties properties = new Properties();

                //setando as propriedades(key) e os seus valores(value)
                properties.setProperty("prop.server.url", url);
                properties.setProperty("prop.server.user", user);
                properties.setProperty("prop.server.password", password);
                

	try {
		//Criamos um objeto FileOutputStream            
		FileOutputStream fos = new FileOutputStream("C:\\DevSoftware\\Sistema\\config.properties");
		//grava os dados no arquivo
		properties.store(fos, "C:\\DevSoftware\\Sistema\\config.properties");
		//fecha o arquivo
		fos.close();
	} catch (IOException ex) {
		System.out.println(ex.getMessage());
		ex.printStackTrace();
	}

       }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
       
       
}