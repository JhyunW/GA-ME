// src/components/CallbackComponent.tsx

import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useUserStore from '../../stores/userStore';
import { fetchKakaoUserInfo } from '../../url/api';

const CallbackComponent = () => {
  const navigate = useNavigate();
  const setUser = useUserStore(state => state.setUser);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    if (code) {
      const fetchUser = async () => {
        try {
          // 서버로부터 accessToken을 포함한 사용자 정보를 가져옵니다.
          const data = await fetchKakaoUserInfo(code);
          if (data) {
            // 가져온 사용자 정보로 스토어의 상태를 업데이트합니다.
            setUser(data.user);
            // isNewUser 상태에 따라 페이지를 리디렉션합니다.
            if (data.user.isNewUser) {
              console.log("/survey로 이동");
              navigate('/survey');
            } else {
              console.log("/로 이동");
              navigate('/');
            }
          }
        } catch (error) {
          alert('또 에러야 또 Authentication failed');
          console.error('Authentication failed:', error);
        }
      };

      fetchUser();
    }
  }, [navigate, setUser]);

  return <div>Loading...</div>;
};

export default CallbackComponent;
