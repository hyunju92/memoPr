# 메모 앱
## 주요 기능   
![화면](https://github.com/hyunju92/memoPr/blob/master/app/src/main/res/drawable/memo_screen_capture.png)   
  > 1. 메모 리스트 - 저장된 메모 보여주기
  > 2. 메모 상세 - 선택한 메모 상세 내용 보여주기. 첨부된 여러장의 이미지는 스와이프로 보여줌.
  > 3. 메모 편집 - 메모의 제목, 내용, 이미지를 편집. 이미지는 카메라/앨범/외부 uri로부터 가져옴.   
  
  
## 프로젝트 구조
![구조](https://github.com/hyunju92/memoPr/blob/master/app/src/main/res/drawable/MEMO%20APP%20Structure.png)   
  > MVVM 패턴사용  
  >
  > - Model
  >   - Room과  Paging의 DataSource 함께 사용하여 db 구성
  >   - kotlin 의 data class를 사용하여, 간편하게 모델 클래스 만듬
  >   - 모델 클래스에서 TypeConverter를 사용하여 Date 타입의 Entity 구성
  >
  > - View
  >   - MainActivity
  >     - 단일 액티비티(MainActivity)와 여러개의 프래그먼트로 구성
  >     - Navigation을 적용하고 MainActivity의 툴바와 싱크를 맞춤
  >     - Navigation으로 프래그먼트를 전환 (전환 시, argument를 활용하여 type safe하게 data를 전달)
  >   
  >   - ListFragment
  >     - 메모 리스트를 보여줄 때에 Paging의 PagedListAdapter를 사용
  >    
  > - ViewModel
  >   - LiveData를 사용하여 View에서 데이터의 변경을 관찰할 수 있게 함
  >   - 코루틴의 viewModelScope에서 비동기 작업(db 접근이 필요한 일들)을 진행 
