# Instrucciones para uso de biblioteca de voz

* Descargar archivos JAR: https://sourceforge.net/projects/freetts/files/
* Descoprimir la carpeta y agregarla al proyecto
* File->Project Structure->Libraries->+->Java
* Agregar las carpetas "lib" y "bin" como librerias al proyecto
* En caso de error null o exception agregar las siguientes lineas de primero en el try (no busca el archivo speech):

System.setProperty("FreeTTSSynthEngineCentral", "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

