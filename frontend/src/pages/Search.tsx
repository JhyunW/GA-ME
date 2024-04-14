import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import useSearchStore from '../stores/searchStore';
import Navbar from "../components/commonUseComponents/Navbar";
import Result from "../components/searchComponents/Result";
import Poket from "../components/commonUseComponents/Poket";
import SearchBox from '../components/searchComponents/SearchBox';
import Title from "../components/searchComponents/Title";
import style from "../components/searchComponents/Search.module.css";

function Search() {
    const location = useLocation();
    const { setSearchPerformed } = useSearchStore();

    // 페이지 로드 또는 경로 변경 시 검색 수행 여부 상태를 초기화
    useEffect(() => {
        setSearchPerformed(false);
    }, [location.pathname, setSearchPerformed]);

    return (
        <>
            <Navbar />
            <Poket />
            <div className="ml-[200px]">
                <Title />
                <SearchBox />
                <h2 className="mb-4 ml-[140px] mt-[50px] font-sejong text-25">검색 결과</h2>
                <hr className={style.hr}></hr>
                <div className="w-4/5 mx-auto">
                    <Result/>
                </div>
            </div>
        </>
    );
}

export default Search;
