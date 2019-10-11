#!/bin/bash

  CURRENT_LOCATION=`pwd`/src
  
  DEMO_PATH="jaxy"
  
  JAXY_TARGET_JAR_NAME="jaxy-coby-thorntail.jar"

  JAXY_FINAL_JAR_NAME="jaxy-thorntail.jar"
  
  cd $CURRENT_LOCATION/dependencies/01_jaxy-api               &&  mvn clean install -Dmaven.test.skip=true && mvn clean ;

  cd $CURRENT_LOCATION/dependencies/02_jaxy-security-provider &&  mvn clean install -Dmaven.test.skip=true && mvn clean ;

  cd $CURRENT_LOCATION/dependencies/03_jaxy-service-discovery &&  mvn clean install -Dmaven.test.skip=true && mvn clean ;
  

  ## Compile certMe for generating letsEncrypt Certificates
  
  cd $CURRENT_LOCATION/dependencies/04_certMe &&  mvn clean install assembly:single -Dmaven.test.skip=true

  CERT_ME_PATH="../../../$DEMO_PATH/lib"

  if [ ! -d "$CERT_ME_PATH" ]; then
 
    mkdir $CERT_ME_PATH
 
  fi
    
  mv target/certMe.jar $CERT_ME_PATH/certMe.jar
    
  mvn clean 

  ## Compile jaxyClient ( that will be downloaded by users from UI ) 
  
  cd $CURRENT_LOCATION/dependencies/05_jaxyClient &&  mvn clean install assembly:single -Dmaven.test.skip=true

  mkdir ../../jaxy/src/main/resources/jaxy-client/ # Create the directory jaxy-client in the jaxy project
  
  mv target/jaxyClient.jar ../../jaxy/src/main/resources/jaxy-client/jaxyClient.jar
  
  mvn clean
  
  cd $CURRENT_LOCATION/jaxy/
  
  if [ "$1" == "m2repo" ]; then
  
      # Compile m2repo Profile 
  
      mvn clean package -Pm2repo -Dmaven.test.skip=true
    
  else
  
    # FatJar Compilation
     
     mvn clean package -Dmaven.test.skip=true
  
  fi
  
  # Copy jaxy-thorntail to $DEMO_PATH
  
  if [ -f "$CURRENT_LOCATION/jaxy/target/$JAXY_TARGET_JAR_NAME" ]; then
 
    if [ -f "$DEMO_PATH/$JAXY_FINAL_JAR_NAME" ]; then
    
        rm $DEMO_PATH/$JAXY_FINAL_JAR_NAME
    
    fi
    
    mv $CURRENT_LOCATION/jaxy/target/$JAXY_TARGET_JAR_NAME ../../$DEMO_PATH/$JAXY_FINAL_JAR_NAME
 
  fi

  mvn clean 
