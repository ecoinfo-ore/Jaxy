
package com.rac021.jaxy.coby.service.logs ;

import com.rac021.jaxy.api.exceptions.BusinessException;
import com.rac021.jaxy.api.streamers.StreamerOutputJsonEncrypted;
import java.io.Writer ;
import java.time.ZoneId ;
import java.util.Locale ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.BufferedWriter ;
import java.time.ZonedDateTime ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.io.OutputStreamWriter ;
import java.time.format.FormatStyle ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.Executors ;
import javax.ws.rs.core.StreamingOutput ;
import java.time.format.DateTimeFormatter ;
import java.util.concurrent.ExecutorService ;

/**
 *
 * @author yahiaoui
 */

public class StreamerLog  implements StreamingOutput {

    final long     timeOunt    = 10000 ; // ( ms) 10 seconds
   
    final ZoneId   frZoneId    = ZoneId.of("Europe/Paris") ;
    
    long           topStart    = 0                         ;

    public Integer INTERVAL    = null                      ;
    
    public String  LOGGER_FILE = null                      ;
    
    final DateTimeFormatter patternDateFr = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.FULL )
                                                             .withLocale(Locale.FRANCE)             ;
    
    
    public StreamerLog( String logger_file, int interval ) {
        this.LOGGER_FILE = logger_file ;
        this.INTERVAL    = interval    ;
    }
    
    @Override
    public void write(OutputStream output) throws IOException     {
       
       System.out.println(" Processing data in StreamerLog ... ") ;
       
       Writer writer = new BufferedWriter ( new OutputStreamWriter(output, "UTF8")) ;

       ExecutorService crunchifyExecutor = Executors.newFixedThreadPool(1) ;
 
       LoggerRunner crunchify_tailF = new LoggerRunner( LOGGER_FILE, INTERVAL ) ;

       crunchifyExecutor.execute ( crunchify_tailF ) ;
       
       String line = null   ;
    
       try {
             while ( true ) {

                 line = crunchify_tailF.logs.poll( INTERVAL, TimeUnit.MILLISECONDS) ;
                 if( line != null ) {
                     writer.write( line  + " \n" )  ;
                     writer.flush()                 ;
                     topStart = 0                   ;
                 } else {
                     if( topStart == 0 ) {
                         topStart = System.currentTimeMillis() ;
                     }
                 }

                 if ( ( topStart != 0 ) && 
                      (System.currentTimeMillis() - topStart >= timeOunt ) ) {
                       
                       ZonedDateTime utcDateZoned = ZonedDateTime.now(frZoneId ) ;

                       writer.write(" \n + " + utcDateZoned.format(patternDateFr) + " \n") ;
                       writer.flush()                        ;
                       topStart = 0                          ;
                 }
                 
             }
       } catch ( IOException ex ) {

            if (ex.getClass().getName().endsWith(".ClientAbortException")) {
                
                try {
                    writer.close()  ;
                    throw new BusinessException("ClientAbortException !! " + ex.getMessage(), ex) ;
                } catch (IOException | BusinessException ex1) {
                    System.out.println( ex1.getMessage() ) ; 
                }
            } else {
                try {
                    writer.close()    ;
                    throw new BusinessException("Exception : " + ex.getMessage()) ;
                } catch (IOException | BusinessException ex1) {
                    System.out.println( ex1.getMessage() )    ;
                }
            }
            
        } catch ( Exception ex) {
            writer.close() ;
            Logger.getLogger(StreamerOutputJsonEncrypted.class.getName()).log(Level.SEVERE, null, ex) ;
            
        } finally {
            writer.close() ;
        }
       
       System.out.println(" CLOSE LOG SERVICE .... ") ;
    }
    
}
