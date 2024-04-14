import React, { useState } from 'react';
import { FaSearch } from 'react-icons/fa';
import useSearchStore from '../../stores/searchStore';
import useUserStore from '../../stores/userStore';
import { searchGames } from '../../url/api';
import style from './Search.module.css';
import Swal from 'sweetalert2';

function SearchBox() {
    const [keyword, setKeyword] = useState('');
    const { setIsLoading, setSearchPerformed } = useSearchStore();

    const user = useUserStore((state) => state.user);
    const setResults = useSearchStore((state) => state.setResults);

    const userId = user?.userId ?? 2;   // 테스트용. 없으면 2로

    const handleSearch = async () => {
        if (!userId) {
            console.log('사용자가 로그인하지 않았습니다.');
            return;
        }

        if (keyword.length < 3) {
            Swal.fire({
                icon: 'error',
                title: '검색 오류',
                text: '검색어는 3글자 이상 입력해주세요.',
            });
            return;
        }
        
        setIsLoading(true); // 검색 시작 시 로딩 상태를 true로 설정
        setSearchPerformed(true); // 검색이 수행되었음을 표시
        console.log("검색 로딩중");
        try {
            const response = await searchGames(keyword, userId);
            if (response.isSuccess) {
                setResults(response.result);
                console.log('검색 결과가 저장되었습니다.');
            } else {
                console.error('검색 실패:', response.message);
            }
        } catch (error) {
            console.error('검색 중 오류 발생:', error);
        }

        setIsLoading(false); // 검색 완료 후 로딩 상태를 false로 설정
        console.log("검색 로딩완료");
    };

    // 엔터 키 입력 감지 함수
    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if(event.key === 'Enter') {
            handleSearch();
        }
    }

    return (
        <div className={style.searchContainer}>
            <input 
                type="text"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onKeyPress={handleKeyPress} // 엔터 키 입력 감지
                placeholder="검색어를 입력하세요(3자 이상)"
                className={style.searchInput}
            />
            <button onClick={handleSearch} className={style.searchButton}>
                <FaSearch />
            </button>
        </div>
    );
}

export default SearchBox;
