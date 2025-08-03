# 📍 Travelist - 나만의 여행 기록 앱

> 여행의 모든 순간을 효율적으로 기록하고,  
> 방문했던 장소와 추억을 한눈에 정리할 수 있는 Android 앱

---

## ✨ 주요 기능 (Key Features)

- 여행 일정 생성 및 색상 지정 (Color Picker)
- 장소별 메모 및 사진 업로드 기능
- Google Map 기반 위치 마커 표시
- 여행별로 기록 필터링 가능
- Naver API 기반 장소 추천 (맛집, 명소, 숙소 등)

---

## 📸 UI (Screenshots)
### ➡️ 여행 추가
<img width="1078" height="507" alt="Image" src="https://github.com/user-attachments/assets/3a2c5e3d-953d-442c-ae46-28f9b5db5afb" />

### ➡️ 마커 추가
<img width="1076" height="595" alt="Image" src="https://github.com/user-attachments/assets/b458e20e-fb1d-4e0e-a870-198dcbfe23f8" />

### ➡️ 여행 기록 보기
<img width="1074" height="587" alt="Image" src="https://github.com/user-attachments/assets/f525f69c-a96b-4082-8133-9b82bfa15b16" />

### ➡️ 장소 추천
<img width="1082" height="599" alt="Image" src="https://github.com/user-attachments/assets/85f2cf7a-7615-4eb2-82e0-7e57825c18cb" />

---

## 🛠 기술 스택 (Tech Stack)

- **Android SDK (Kotlin)**
- **MVVM 아키텍처**
- **Room (로컬 DB)**
- **Google Map API / Places API**
- **Naver Local Search API**
- **Retrofit + Gson**
- **Color Picker 라이브러리**
- **RecyclerView / ViewModel / LiveData**

---

## 📁 디렉토리 구조 (요약)

```
ddwu.com.mobile.myapplication/
├── data/
│   ├── dao/          # NoteDao, TripDao
│   ├── database/     # AppDatabase
│   ├── model/        # Note, Trip
│   ├── network/      # NaverApi, RetrofitInstance
│   └── repository/   # NoteRepository, TripRepository
├── ui/
│   ├── adapter/      # ImageAdapter, TripAdapter 등
│   ├── viewmodel/    # NoteViewModel, TripViewModel
│   └── activity/     # MainActivity, AddTravelActivity 등
```

---

## 🔍 주요 기능 상세

### 1. 여행 추가 기능
- 여행 이름, 기간, 위치, 테마 색상 입력
- Google Map 기반 지도에 위치 등록
- 사용자 지정 색상 저장 (ColorPicker 사용)

### 2. 여행 기록 보기
- 방문 장소 및 사진 기록 업로드
- 각 여행별 필터링하여 추억 관리 가능
- 사진 클릭 시 상세 메모 확인

### 3. 장소 추천 기능
- Naver API 기반으로 주변 맛집, 명소, 숙소 추천
- 검색어 입력 기반 추천 결과 제공

---

## 🚀 실행 방법 (Getting Started)

### 1. 프로젝트 클론
```bash
git clone https://github.com/your-repo/travelist.git
```

### 2. Android Studio에서 열기
- Android Studio에서 `File > Open` 클릭 후 프로젝트 폴더 선택

### 3. API Key 설정
`local.properties` 또는 `.env.properties` 파일에 다음 키 추가:

```
GOOGLE_MAPS_API_KEY=your_google_api_key
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret
```

---

## 🛠 향후 개선 방향 (Future Plans)

| 항목 | 계획 |
|------|------|
| 🔐 로그인 기능 | 사용자별 데이터 관리, 콘텐츠 작성 권한 부여 |
| ☁️ 클라우드 백업 | Firebase 기반 데이터 동기화 |
| ⭐ 장소 리뷰 및 평점 | 사용자 간 공유, 장소 신뢰도 향상 |
| 🔄 여행 공유 | 유저 간 추천 루트 공유 기능 |

---

## 👩‍💻 개발자 정보

| 이름 | 역할 |
|------|------|
| 강민주 (20220742 컴퓨터학과) | 기획 · 설계 · 전체 개발 담당 |

---

## 📄 라이선스

해당 프로젝트는 학술 과제용으로 작성되었으며, 외부 배포 시 별도의 라이선스를 명시해주세요.
