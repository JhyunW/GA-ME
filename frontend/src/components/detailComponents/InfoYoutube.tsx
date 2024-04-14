import React, { useEffect, useState } from 'react';
import styles from './InfoYoutube.module.css';
import axios from 'axios';
import { api, log } from '../../url/api';


// Props 인터페이스 정의
interface InfoYoutubeProps {
  userId?: number;
  gameId: number;
  gameName: string;
}

interface VideoItem {
  kind: string;
  etag: string;
  id: {
    kind: string;
    videoId: string;
  };
  snippet: {
    publishedAt: string;
    channelId: string;
    title: string;
    description: string;
    thumbnails: {
      default: {
        url: string;
        width: number;
        height: number;
      };
      medium: {
        url: string;
        width: number;
        height: number;
      };
      high: {
        url: string;
        width: number;
        height: number;
      };
    };
    channelTitle: string;
    liveBroadcastContent: string;
    publishTime: string;
  };
}

interface YoutubeSearchResponse {
  kind: string;
  etag: string;
  nextPageToken: string;
  regionCode: string;
  pageInfo: {
    totalResults: number;
    resultsPerPage: number;
  };
  items: VideoItem[];
}

// DetailInfo 컴포넌트를 정의합니다.
const InfoYoutube: React.FC<InfoYoutubeProps> = ({ userId, gameId, gameName }) => {
  const API_KEY = import.meta.env.VITE_YOUTUBE_API_KEY;
  const [videos, setVideos] = useState<VideoItem[]>([]);
  // const [showButton, setShowButton] = useState(true);

  const handleClickYoutube = async (): Promise<void> => {
    try {
      const response = await axios.get<YoutubeSearchResponse>('https://www.googleapis.com/youtube/v3/search', {
        params: {
          key: API_KEY,
          q: `${gameName} trailer`,
          // order:'viewCount', //rating
          relevanceLanguage: 'ko',
          part: 'snippet',
          maxResults: 10, // 변경 가능
          type: 'video',
          videoEmbeddable: 'true',
        }
      });
      console.log(response.data.items, gameId, gameName)
      setVideos(response.data.items);
      // setShowButton(false); // 버튼 숨기기
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }

  useEffect(() => {
    handleClickYoutube();
  }, [gameId]); // 빈 배열을 전달하여 컴포넌트가 마운트될 때 한 번만 실행되도록 함

    if (userId) {
      //가중치 증가(detail 이동)
      api.put("/tracking?userId=" + userId+"&gameId="+gameId+"&action=video-play");
      
      // 사용자 패턴 로그
      log(userId, "detail", "click", [
        { move_page: "video-play" },
        { game_id: gameId },
      ]);
    }

  return (
    <>
    <div className={styles.container}>
      <div className={styles.title}>공식 트레일러</div>
      {/* {showButton && <button onClick={handleClickYoutube}>관련 영상 보기</button>} */}
      <div>
        {videos.length > 0 && (
          <div key={videos[0].id.videoId}>
            <iframe
              title={videos[0].snippet.title}
              width="711"
              height="400"
              src={`https://www.youtube.com/embed/${videos[0].id.videoId}`}
              allowFullScreen
            ></iframe>
            <p className={styles.youtubeName}>{videos[0].snippet.title}</p>
          </div>
        )}
      </div>
    </div>
  </>
  );
}

export default InfoYoutube;



