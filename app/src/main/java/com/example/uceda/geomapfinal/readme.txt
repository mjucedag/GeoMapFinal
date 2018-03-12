##Principales Problemas - No carga de Google Maps. Pantalla en blanco solo con el logotipo Google abajo a la izquierda##

Investigando sobre ese problema, he encontrado a mucha gente con el mismo problema, y la mayoría los solucionaba o bien,
re-generando la API key, o incluso el proyecto entero. Otros fueron tocando una configuracion en el AVD, la cual en mi version
de Android Studio no aparece ... Al final, viendo en los LOG, el error que indicaba es que no es encontrado la libreria
OpenGL ES 2.0, la cual es necesitada. Link donde indica tambien un usuario con el mismo problema:

https://stackoverflow.com/a/47846455

La mayoría (por no decir todos) los emuladores de dispositivos Android proporcionados por Android Studio no proporcionan
compatibilidad con la libreria OpenGL ES 2.0, la cual es fundamental para la nueva version de Google Maps.

Nota: https://stackoverflow.com/questions/8941016/android-opengl-es-2-0-emulator | en ese enlace hablan que Android Studio
ya incorpora emuladores con esa librería, aunque de todos lo que he probado, ninguno tenía, y no he podido recabar más información
sobre esto. El código en Runtime para saber si el dispositivo es compatible con esa libreria es el siguiente:

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

Hasta aquí, la resolucion a este problema fue intentar desplegar en un dispositivo real, los cuales si soportan
esa libreria, pero tambien seguia teniendo el mismo problema. Dicho problema ahora venia dado porque el movil disponia de la API 17

Dicho esto, opté por bajarme un emulador aparte de Android Studio, el más completo, y gratis (aunque solo sea los 30
primeros días), es Genymotion ( https://www.genymotion.com/ ). La instalación es sencilla, registrarte en la página,
bajarme el instalador para windows, junto con Virtual Box ya que se apoya en él para emular el dispositivo. Aparte, en mi
Android Studio me descargo el PlugIn de Genymotion para que lo reconozca, y configuro mi primer emulador, como:
Google Nexus 5X - 6.0.0 - Api23. Perfecto, y ahora me encuentro otro problema, en Genymotion no soporta Google Play Services.
Investigando, me encuentro que ya hay compañeros que han creado los instaladores a mano, y que tan solo flasheando (o más
comunmente, arrastrando (drag and drop) el .zip en mi emulador, me los instala. El enlace de interes:
https://stackoverflow.com/questions/33344857/preview-google-nexus-5x-6-0-0-api-23-gapps
Haciendo todos esos pasos, ya tengo mi emulador preparado, ejecuto mi GeoMapsFinal en dicho emulador, y por fin, me aparece
mi GoogleMaps

Aun así, cada máquina tiene su propio debug keystore. por eso, es fundamental desplegar el proyecto en el mismo dispositivo
para ver que funciona.

## Segundo gran problema!!! - App crashes (sometimes) with Fatal signal 11 (SIGSEGV), code 1
Este segundo gran error me viene, cuando leia de mi base de datos, las localizaciones guardadas. Me lanzaba ese error el emulador.
Las primeras pruebas que he estado realizando, para verificar si el error era de mi base de datos, o del emulador, o de alguna version
de la API. He probado a cargar la base de datos en otros dispositivos, y no he tenido ningun problema. Asi que leer de la base de datos,
solo me esta dando problemas con Genymotion.
Con esto, me pregunte dos cosas: ¿puede ser problema de compatibilidad?, ¿o puede ser problema de que los datos que estan en la base de datos,
sean datos que generen conflicto?
https://stackoverflow.com/questions/37101603/app-crashes-sometimes-with-fatal-signal-11-sigsegv-code-1
Ese enlace indica que las coordenadas introducidas pueden crashear la aplicacion. Debuggeando, veo que lee los datos correctamente,
pero que al intentar pintarlos en el mapa, es cuando crashea.
Despues de muchas pruebas (hago serializabe mi clase, pongo en otro hilo la carga de datos, llamo al colector de basura
 despues de leer los datos por si fuera problema de memoria, etc etc ...) pienso que puede que las
coordenadas generadas por Genymotion generen algun tipo de conflicto (¿?), asi que en vez de guardar el objecto Location entero como tal
en mi base de datos, guardo unicamente latitud y longitud, que es lo que me hace falta para Google Maps (resolver el problema reduciendo al
mínimo, o como es conocido, reduccion a la aburda). Modificando mi objecto solo con valores primitivos ...
Asi que despues de modificar mi objeto Localizacion, ya por fin me lee de la base de datos sin ningun tipo de crasheo

##Datos de prueba:
2018/03/11 -
2018/03/12

Son datos a traves del GPS del emulador, el cual hay que ponerle las coordenadas a mano. Por ello, se ven esos saltos tan "picuos", no un desplazamiento
más normal, ya que con el movil, si te vas moviendo de una manera natural.