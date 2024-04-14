import { motion } from 'framer-motion';
import NewsCard from '../HotTopicComponents/NewsCard';
import useHotTopicStore from "../../stores/hotTopicStore";
import style from './NewsList.module.css';
import useUserStore from '../../stores/userStore';

// 사용 스토어의 구조를 기반으로 하는 구조
interface NewsList {
  hotTopicLink: string;
  hotTopicImg: string;
  hotTopicTitle: string;
  hotTopicShortDesc: string;
  hotTopicDate: Date;
}

const NewsList: React.FC = () => {
  const { newsData, nLoading, nError ,fetchNewsData} = useHotTopicStore();
  const {isLoggedIn, user} = useUserStore();
     // 버튼 클릭 시 뉴스 데이터를 다시 가져오는 함수
  const handleRetry = () => {
    fetchNewsData(user?.userId??0); // 데이터 재요청
  };
  if(!isLoggedIn){
    return <div className={`${style.container}`}>
    <div className={`${style.eyes}`}></div>
    <div>
    <h1 className={`${style.loadingTitle2}`}> 로그인이 필요한 페이지입니다 . </h1>   
    </div>
</div>;
  }
  if (nLoading) {
    return <div className={`${style.container}`}>
              <div className={`${style.loader}`}></div>
              <div>
              <h1 className={`${style.loadingTitle}`}> 뉴스 정보를 가져오는 중입니다. </h1>  
              <h1> 조금만 기다려주세요 ! </h1>  
              </div>
          </div>;
  }

  if (nError) {
    return <div className={`${style.container}`}>
    <div className={`${style.eyes}`}></div>
    <div>
    <h1 className={`${style.loadingTitle}`}> 뉴스 정보를 가져오는데 실패했습니다. </h1>  
    <button className={`${style.outlineButton}` }onClick={handleRetry}> 다시 시도하기 </button>
    </div>
</div>;
  }

  if (!newsData || !newsData.result.length) {
    return <div>No data available</div>;
  }

  return (
    <motion.ul className="grid gap-2 grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-2 xl:grid-cols-3"
      variants={{
        hidden: {},
        visible: { transition: { staggerChildren: 0.1 } }
      }}
      initial="hidden"
      animate="visible"
    >
      {newsData.result.map((news: NewsList, index: number) => (
        <motion.li key={index} className="list-none"
          variants={{
            hidden: { x: -60, opacity: 0 },
            visible: { x: 0, opacity: 1, transition: { duration: 0.3 } }
          }}
        >
          <NewsCard
            key={index}
            hotTopicLink={news.hotTopicLink}
            hotTopicImg={news.hotTopicImg}
            hotTopicTitle={news.hotTopicTitle}
            hotTopicShortDesc={news.hotTopicShortDesc}
            hotTopicDate={new Date(news.hotTopicDate)}
          />
        </motion.li>
      ))}
    </motion.ul>
  );
};

export default NewsList;