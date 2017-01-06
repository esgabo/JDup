package net.guajava.jdupmaster.configuration;
;
import javafx.stage.DirectoryChooser;
import net.guajava.jdupmaster.utils.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@SpringBootApplication(scanBasePackages = {"net.guajava.jdupmaster.services", "net.guajava.jdupmaster.fxml"})
public class Config {
    @Bean
    public FileDigest getFileDigest() {
        return new Md5FileDigest();
    }

    @Bean
    public FileSizeFormatter getFileSizeFormatter(NumberFormat decimalFormat) {
        return new FileSizeAutoFormatter(decimalFormat);
    }

    @Bean
    public NumberFormat getNumberFormat() {
        return new DecimalFormat("0.00");
    }

    @Bean
    public ResourceManager getResourceManager(ApplicationContext applicationContext) {
        return new ResourceManager(applicationContext);
    }

    @Bean
    public UIHelper getUIHelper() {
        return new UIHelper();
    }


}
