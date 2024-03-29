# 메모 앱
## 주요 기능   
![화면](https://github.com/hyunju92/memoPr/blob/master/app/src/main/res/drawable/memo_screen_capture.png)
>   1. 메모 리스트 - 저장된 메모 보여주기     
>   2. 메모 상세 편집 - 선택한 메모의 제목, 내용, 이미지를 편집 (이미지는 카메라/앨범로부터 가져옴. 여러장의 이미지 첨부 가능)


## 프로젝트 구조
![구조](https://github.com/hyunju92/memoPr/blob/master/app/src/main/res/drawable/memo_app_structure.png)
  ### MVVM 패턴 사용  
  View와 ViewModel의 역할을 구분함. Activity와 Fragment에 가중됐던 작업을 ViewModel에 이관.      
  뷰와 로직사이의 의존성이 사라져서, 모듈화된 개발 가능
  >
  > - Model
  >   - room과 paging DataSource를 활용하여 db 구성
  >   - MemoDao의 function return 값을 rx의 Observable로 정의
  >   - Repository에서 db작업 수행함 (ViewModel에서 이를 주입받아 사용함)
  >
  > - View
  >   - ViewModel의 LiveData, Observable 변수를 구독하여 메모 값의 변경, 이벤트의 발생 등을 관찰해 화면에 반영함
  >   - Navigation 적용. 화면 전환 시, argument를 활용하여 type safe하게 data를 전달
  >   - 메모 리스트를 보여줄 때에 Paging의 PagedListAdapter를 사용
  >    
  > - ViewModel
  >   - ViewModel에서 메모 데이터를 LiveData로 갖고 있음 (View에서 이를 구독하여 관찰)
  >   - ViewModel에서 rx-Subject로 화면 이벤트 발생에 대한 객체를 갖고 있음 (View에서 이를 구독하여 관찰)
  >   - Repository를 주입받아 db작업을 수행하는데 이때 rx를 활용하여 비동기로 수행


## Android 11 범위저장소 정책 대응
  > - 현재 targetSdkVersion 30으로, 설치 단말 Android 10 이상에선 Scoped Storage가, Android 9 이하에선 Legacy External Storage가 적용됨
  > - 앨범/카메라로부터 이미지를 가져오는 기능은 저장소에 대한 접근이 발생하므로 대응이 필요
  >     - 앨범에서 이미지 가져오기 (외부저장소에 저장된 이미지 uri 복사)
  >         - 10 이상 : [ 외부저장소 - Media부분(공용공간) ]로부터 -> [ 외부저장소 - Media부분(해당 앱 개별공간) ]으로 복사함
  >         - 9 미만 : [ 외부저장소 - 공용 저장공간 ]으로부터-> [ 내부저장소(일반파일 공간) ]로 복사함
  >     - 카메라 촬영 (저장소에 이미지 uri 생성)
  >         - 10 이상 : [ 외부저장소 - Media부분(해당 앱 개별공간) ]에 저장
  >         - 9 미만 :  [ 내부저장소(일반파일 공간) ]에 저장


## 학습
  >  - MVVM 디자인 패턴의 적용
  >  - Hilt를 사용해서 의존성 주입 (model->viewModel->View)
  >  - RxJava 활용하여 비동기 작업 처리
  >  - ViewModel, LiveData, Paging 등 AAC 라이브러리 적용
  >  - Room, Navigation 등 Jetpack 라이브러리 적용
  >  - 코틀린 사용에 익숙해지기

## 앞으로 학습하고 싶은 부분
  >  - 비동기 작업에 코루틴 사용해보기
  >  - Unit 테스트 적용