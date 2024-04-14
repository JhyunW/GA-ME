// 담당자 : 장현욱


import { useEffect } from 'react'
import useStoreMain from "../../stores/mainStore";
import { useNavigate } from 'react-router-dom';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, Pagination, Navigation, FreeMode, EffectCoverflow } from 'swiper/modules';
import { motion } from "framer-motion";
import style from './Banner.module.css'
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/thumbs';
import 'swiper/css/pagination';


// 베너 타입스크립트 정의
interface Banner {
  gameId: number;
  gameName: string;
  gameHeaderImg: string;
  gamePriceInitial: number;
  gamePriceFinal: number;
  gameDeveloper: string;
  gameLike: number | null;
  isPrefer: boolean;
  tagList: Array<{ codeId: string; tagId: number; tagName: string }>;
}


const Banner: React.FC = () => {
  const { bannerData, mainBanner } = useStoreMain();
  const navigate = useNavigate(); // useNavigate 인스턴스화

  useEffect(() => {
    mainBanner(); // 마운트시 데이터 가져오기
  }, [mainBanner]);

  // 해당 게임 디테일 페이지로 이동
  const handleClickBanner = (gameId: number) => {
    navigate(`/detail/${gameId}`)
  }
  // 파일이 없는경우를 정확히 검사
  if (bannerData && bannerData.result && bannerData.result.length > 0) {
    return (
      <div className="relative w-full overflow-hidden h-60vw"> {/* 베너 컨테이너 */}

        {/* 베너 메인 */}
        <Swiper
          className={`${style.swiperCustom} w-full h-full`}
          // 사용모듈정의
          modules={[FreeMode, Autoplay, Pagination, Navigation, EffectCoverflow]}
          // 그랩후 슬라이드 가능여부
          grabCursor={true}
          centeredSlides={true}
          // 양옆 미리보기 효과 주기
          effect={'coverflow'}
          // 미리보기 효과에 대한 정의
          coverflowEffect={{
            rotate: 60,
            stretch: 0,
            depth: 100,
            modifier: 1,
            slideShadows: false,
          }}
          // 한 슬라이드당 보여줄 이미지 갯수
          slidesPerView={2}
          // 슬라이드 사이 간격
          spaceBetween={0}
          // 무한루프 여부
          loop={true}
          // 바로가기
          navigation={true}
          // 자동 슬라이드 넘김
          autoplay={{
            delay: 5000,
            disableOnInteraction: true,
          }}
          // 옆의 슬라이드 보기
          slideToClickedSlide={true} // 클릭한 슬라이드로 이동
        >
          {bannerData?.result.map((banner: Banner, index: number) => (
            <SwiperSlide key={index} className="relative h-full">
              {/* 뒷배경 */}
              <div className="absolute w-full h-full bg-cover bg-center filter blur-md z-[-1] before:content-[''] before:absolute before:inset-0 before:bg-black before:bg-opacity-50" style={{ backgroundImage: `url(${banner.gameHeaderImg})` }}></div>
              {/* 메인베너 이미지 */}
              <div className="relative w-full h-5/6 flex justify-center items-start mt-16">
                <img src={banner.gameHeaderImg} alt={banner.gameName} className="mb-8 w-9/10 h-80 object-fill rounded-xl" />
                {/* 자세히 보기 버튼 정의 */}
                <motion.div
                  className={`absolute bottom-10 right-3 transform bg-transparent backdrop-blur-md text-white font-sejong text-sm py-2 px-4 border rounded-full cursor-pointer`}
                  initial={{ opacity: 0.5 }} // 초기 opacity 값을 0.5로 설정
                  whileHover={{ scale: 1.2, opacity: 1 }} // 호버 시 scale을 1.2로, opacity를 1로 변경
                  transition={{ duration: 0.3 }} // 애니메이션 지속 시간을 0.3초로 설정
                  onClick={() => handleClickBanner(banner.gameId)}
                >
                  자세히 보기
                </motion.div>
              </div>
            </SwiperSlide>
          ))}
        </Swiper>

      </div>
    );
  }
};

export default Banner;
