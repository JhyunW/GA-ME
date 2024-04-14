// import { motion } from 'framer-motion';
import style from './Title.module.css'

const Title: React.FC = () => {
    return (
        <div className={`${style.titleDiv}`}>
            <img className={`${style.icon}`} src='./HotTopicFireIcon.png'></img>
            <h1 className={`${style.title}`}>Hot Topic</h1>
            <p className={`${style.detail}`}>최신 게임 소식을 확인하세요 ! </p>
        </div>
    );
};

export default Title;