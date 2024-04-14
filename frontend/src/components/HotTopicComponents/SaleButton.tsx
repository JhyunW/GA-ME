import React from 'react';
import style from './HotTopicButton.module.css';

interface SaleButtonProps {
    onClick: () => void; // onClick 속성의 타입을 () => true로 지정
}

const SaleButton: React.FC<SaleButtonProps> = ({ onClick }) => {
    return (
        <button className={style.topicBtn} onClick={onClick}>
            <div className={style.container}>
                <img src="./HotTopicSaleIcon.png" alt="Sale Icon" />
                <p>세일</p>
            </div>
        </button>
    );
};

export default SaleButton;
