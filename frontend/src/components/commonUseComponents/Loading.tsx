// 담당자 : 장현욱

const Loading = () => {
    return (
        <div className="fixed inset-0 z-50 bg-gray-900 bg-opacity-50 backdrop-blur-sm flex justify-center items-center">
            <img src="/GGameLoading.gif" alt="GGameLoading" />
        </div>
    );
};

export default Loading;
