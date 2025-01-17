
package com.rac021.jaxy.coby.service.clean.output ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import javax.inject.Inject ;
import javax.ws.rs.Produces ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.coby.io.Writer;
import com.rac021.jaxy.coby.checker.TokenManager;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry;
import com.rac021.jaxy.api.qualifiers.security.Policy;
import com.rac021.jaxy.api.qualifiers.security.Secured;
import com.rac021.jaxy.coby.service.configuration.CobyConfiguration ;

/**
 *
 * @author R.Yahiaoui
 */

@ServiceRegistry("coby_clean_output")
@Secured(policy = Policy.CustomSignOn )
// @Cipher( cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC } )

public class CobyService    {
 
    @Inject
    CobyConfiguration configuration ;
    
    @PostConstruct
    public void init() {
    }

    public CobyService() {
    }
    
    @GET
    @Produces( {  "xml/plain" } )
    public Response cancel ( @HeaderParam("API-key-Token") String token  , 
                             @HeaderParam("keep") String filterdIndex    , 
                             @Context UriInfo uriInfo ) throws Exception {    
         
        //executorService.shutdownNow() ;
        
        String login     = TokenManager.getLogin(token) ;
        String path_data = TokenManager.buildOutputFolder(configuration.getOutputDataFolder(), login ) ;
        
        Writer.removeDirectory( path_data ) ;
        return Response.status(Response.Status.OK)
                       .entity("\n Coby Output Directory Cleaned \n" )
                       .build() ;      
    }
  
}
