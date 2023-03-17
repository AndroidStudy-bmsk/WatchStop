# WatchStop

스톱워치 앱

### 🔧 Used Technologies

- AlertDialog
- timer
- ToneGenerator

## [Thread](https://developer.android.com/guide/components/processes-and-threads?hl=ko#Threads)

![thread](https://user-images.githubusercontent.com/24618293/198324676-b592f822-cd9d-40e7-a8cf-04a5e0e46dc1.png)

작업자 스레드(Worker Thread)에서 UI를 조작해야 하는 경우 고려할 수 있는 방법
- Activity.runOnUiThread(Runnable)
- View.post(Runnable) : zero delay
- View.postDelayed(Runnable, long) : long = delay
- Handler
