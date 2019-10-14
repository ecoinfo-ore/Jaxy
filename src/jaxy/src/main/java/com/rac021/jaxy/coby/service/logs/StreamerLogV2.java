
package com.rac021.jaxy.coby.service.logs ;

import java.io.Writer ;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException ;
import java.io.OutputStream ;
import java.nio.file.WatchKey;
import java.io.BufferedReader;
import java.util.stream.Stream;
import java.util.logging.Level;
import java.io.BufferedWriter ;
import java.util.logging.Logger;
import java.nio.file.WatchEvent;
import java.nio.file.FileSystems;
import java.nio.file.WatchService ;
import java.io.OutputStreamWriter ;
import javax.ws.rs.core.StreamingOutput ;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardWatchEventKinds;
import com.rac021.jaxy.api.exceptions.BusinessException;

/**
 *
 * @author yahiaoui
 */

public class StreamerLogV2  implements StreamingOutput {

    public String  LOGGER_FILE = null                ;
    
    public StreamerLogV2( String logger_file ) {
        this.LOGGER_FILE = logger_file       ;
    }
    
    @Override
    public void write(OutputStream output) throws IOException     {
       
       System.out.println(" Processing data in StreamerLog ... ") ;
       
       Path logFile = Paths.get(LOGGER_FILE ) ;
       
       int characters = 0 ;

       Writer writer = new BufferedWriter ( new OutputStreamWriter(output, "UTF8")) ;
       
       try {
            
            try (Stream<String> stream = Files.lines(logFile)) {

	        stream.forEach(line -> {
                    try {
                         writer.write(line + "\n") ;
                         writer.flush()            ;
                    } catch ( IOException ex )     {
                        Logger.getLogger(StreamerLogV2.class.getName()).log(Level.SEVERE, null, ex) ;
                        try {
                            writer.close()      ;
                        } catch (IOException ex1) {
                            Logger.getLogger(StreamerLogV2.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    } 
                } ) ;

	    } catch (IOException e) {
	      e.printStackTrace()   ;
	    }
                
            WatchService watcher = FileSystems.getDefault().newWatchService();

            logFile.toAbsolutePath().getParent().register( watcher, StandardWatchEventKinds.ENTRY_MODIFY ) ;

            do {
                
                WatchKey key = watcher.take() ;
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path path = pathEvent.context();
                    if (logFile.endsWith(path)) {
                        try (BufferedReader in = Files.newBufferedReader(logFile , StandardCharsets.UTF_8 ) ) {
                            String line           ;
                            in.skip( characters ) ;
                            while ((line = in.readLine()) != null) {
                                characters += line.length() + System.lineSeparator().length() ;
                                writer.write(line + "\n") ;
                                writer.flush()            ;
                            }
                        }
                    }
                }
                
                key.reset() ;
                
            } while (true)  ;
            
        } catch (IOException | InterruptedException ex) {
            
             if (ex.getClass().getName().endsWith(".ClientAbortException")) {
                
                try {
                    throw new BusinessException("ClientAbortException !! " + ex.getMessage(), ex) ;
                } catch (BusinessException ex1) {
                    System.out.println( ex1.getMessage() ) ; 
                }
            } else {
                try {
                    throw new BusinessException("Exception : " + ex.getMessage()) ;
                } catch (BusinessException ex1) {
                    System.out.println( ex1.getMessage() ) ;
                }
            }        
        }
       
       System.out.println(" CLOSE LOG SERVICE .... ") ;
    }
}
