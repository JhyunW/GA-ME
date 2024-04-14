import React from 'react';
import style from './HotTopicButton.module.css';

interface NewsButtonProps {
    onClick: () => void; // onClick 속성의 타입을 () => void로 지정
}

const NewsButton: React.FC<NewsButtonProps> = ({ onClick }) => {
    return (
        <button className={style.topicBtn} onClick={onClick}>
            <div className={style.container}>
                <img src="./HotTopicNewsIcon.png" alt="News Icon" />
                <p>뉴스</p>
            </div>
        </button>
    );
};

export default NewsButton;
