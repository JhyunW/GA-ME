// 담당자 : 장현욱

import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 추가
import GameCard from '../commonUseComponents/GameCard'; // 게임카드 컴포넌트
import useStoreMain from "../../stores/mainStore"; // 메인데이터(게임) 불러오는 스토어
import useStoreCurrentPage from '../../stores/currentPage'; // 현재 페이지 확인할 스토어
import LoadingComponent from '../commonUseComponents/Loading'; // 로딩페이지 컴포넌트
import style from './Game.module.css'
import { AxiosError } from 'axios';


// 사용 스토어의 구조를 기반으로 하는 구조
interface Game {
  gameId: number;
  gameName: string;
  gameHeaderImg: string;
  gamePriceInitial: number
  gamePriceFinal: number;
  gameDeveloper: string;
  tagList: Array<{ codeId: string; tagId: number; tagName: string }>;
  isPrefer: boolean;
  gameLike: number | null;
}


const GameComponent: React.FC = () => {
  const { data, loading, error, fetchMainData, fetchUserGameData, userGameData, page, setPage } = useStoreMain();
  const navigate = useNavigate(); // useNavigate 인스턴스화
  const { nowCategory } = useStoreCurrentPage();
  const [nowGameList, setNowGameList] = useState<Game[]>([]); // nowGameList 상태 추가

  // 현재 선택되어진 카테고리 확인
  useEffect(() => {
    if (nowCategory === '전체 인기') {
      fetchMainData();
    } else if (nowCategory === '취향 저격') {
      fetchUserGameData();
    }
  }, [fetchMainData, fetchUserGameData, nowCategory, page]);

  useEffect(() => {
    // 데이터가 업데이트될 때마다 nowGameList 업데이트
    if (nowCategory === '전체 인기') {
      // data.result가 ApiResult[] 타입인지 확인
      if (data && Array.isArray(data.result)) {
        setNowGameList(data.result);
      }
    } else if (nowCategory === '취향 저격') {
      // userGameData가 null이 아니고, userGameData.result에 gameCardDtoList가 존재하는지 확인
      if (userGameData?.result.gameCardDtoList) {
        setNowGameList(userGameData.result.gameCardDtoList);
      }
    }
  }, [data, userGameData, nowCategory]);

  if (loading) { // 게임 데이터 로딩동안 보여줄 페이지 정의
    return <LoadingComponent />;
  }

  if (error) {
    const axiosError = error as AxiosError;
    return <div>Error: {axiosError.message}</div>;
  }

  if (!data || !data.result) {
    return <div>No data available</div>;
  }

  const handleClickGame = (gameId: number) => {
    navigate(`/detail/${gameId}`)
  }

  const handleNextPage = () => {
    setPage(page + 1); // 다음 페이지로 이동
  };

  const handlePrevPage = () => {
    if (page > 1) setPage(page - 1); // 이전 페이지로 이동 (1보다 클 때만)
  };

  return (
    <>
      <motion.ul className="grid gap-1 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5"
        variants={{
          hidden: {},
          visible: { transition: { staggerChildren: 0.1 } }
        }}
        initial="hidden"
        animate="visible"
      >
        {nowGameList.map((game: Game, index: number) => (
          <motion.li key={index} className="list-none"
            variants={{
              hidden: { x: -60, opacity: 0 },
              visible: { x: 0, opacity: 1, transition: { duration: 0.1 } }
            }}
          >
            {/* 게임 카드 데이터 지정해서 넣어주기 */}
            <GameCard
              key={game.gameId}
              gameId={game.gameId}
              imageUrl={game.gameHeaderImg}
              title={game.gameName}
              developer={game.gameDeveloper}
              beforPrice={`₩ ${game.gamePriceInitial / 100}`}
              price={`₩ ${game.gamePriceFinal / 100}`}
              tagsAll={game.tagList}
              tags={game.tagList.filter(tag => tag.codeId === "GEN" && tag.tagName.length < 7).map(tag => tag.tagName) ?? []}
              isPrefer={game.isPrefer}
              likes={game.gameLike}
              onGameClick={handleClickGame} // 디테일 페이지 이동
            />
          </motion.li>
        ))}
      </motion.ul>
      {/* 페이지 이동 버튼 */}
      {/* nowCategory 가 '취향 저격' 일경우에만 보이게 하기 */}
      {nowCategory === '전체 인기' && (
      <div className={`${style.container} p-7 flex justify-center items-start`}>
        <div className={`${style.pane}`}>
          <button className={`${style.label} pb-2`} onClick={handlePrevPage} disabled={page <= 1}>
            <span> 이전 </span>
            <input id="left" className={`${style.input}`} name="radio" type="radio" />
          </button>
          <label className={`${style.label}`}>
            <span>{page}</span>
            <input id="middle" className={`${style.input}`} defaultChecked={true} name="radio" type="radio" />
          </label>
          <button className={`${style.label} pb-2`} onClick={handleNextPage}>
            <span> 다음 </span>
            <input id="right" className={`${style.input}`} name="radio" type="radio" />
          </button>
          <span className={`${style.selection}`}></span>
        </div>
      </div>
      )}
    </>
  );
};

export default GameComponent;
