// @ts-check

document.addEventListener('DOMContentLoaded', () => {
    const currentDatetimeArea = document.getElementById('current-datetime-area');
    const currentDate = document.getElementById('current-date');
    const currentTime = document.getElementById('current-time');

    // サーバの時刻とクライアントの時刻の差を求める
    const serverDatetimeStr = currentDatetimeArea?.dataset.serverDatetime || new Date().toISOString();
    const serverDatetime = new Date(serverDatetimeStr).getTime();
    const clientLoadDatetime = new Date().getTime();
    const timeOffset = serverDatetime - clientLoadDatetime;

    // 1秒ごとに現在時刻を更新
    setInterval(updateClock, 1000);
    updateClock();

    /**
     * 現在時刻を更新する
     */
    function updateClock() {
        const now = new Date();
        const adjustedTime = new Date(now.getTime() + timeOffset);

        if (!currentDate || !currentTime) return;

        // yyyy/MM/dd (ddd)の形式で現在日に表示
        currentDate.textContent = adjustedTime.toLocaleString("ja-JP", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            weekday: "short"
        });

        // HH:mmの形式で現在時刻に表示
        currentTime.textContent = adjustedTime.toLocaleTimeString("ja-JP", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false
        });
    }
});