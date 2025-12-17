## Описание

Приложение для изучения Jetpack Compose и работы с аудио при помощи Media3 ExoPlayer.
Позволяет воспроизводить mp3 файлы с устройства. Для запроса аудио используется MediaStore API.


### Главный экран
Отображает mp3 файлы с устройства, позволяет выбрать файл для воспроизведения.

<img alt="HomeScreen.gif" height="550" src="gifs/HomeScreen.gif" width="250"/>

При повторном нажатии на треке воспроизведение приостанавливается.

<img alt="HomeScreen - same item reselected.gif" height="550" src="gifs/HomeScreen%20-%20same%20item%20reselected.gif" width="250"/>

Плеер в нижней части экрана позволяет приостановить/продолжить воспроизведение нажатием на кнопку.
При нажатии на самом плеере происходит переход на экран плеера.

<img alt="HomeScreen - bottom player.gif" height="550" src="gifs/HomeScreen%20-%20bottom%20player.gif" width="250"/>


### Экран плеера
Отображает информацию о треке и позволяет управлять воспроизведением.
Progress Bar для отображения состояния воспроизведения в настоящий момент находится в разработке.

<img alt="PlayerScreen.gif" height="550" src="gifs/PlayerScreen.gif" width="250"/>


### Работа в фоне
Воспроизведение происходит в фоне при помощи MediaSessionService.
Во время воспроизведения отображается соответствующее уведомление, которое также позволяет управлять воспроизведением.

<img alt="PlaybackService.gif" height="550" src="gifs/PlaybackService.gif" width="250"/>




