import {Notification} from "@/lib/notificationApiCaller";

/**
 * 알림 카드 컴포넌트
 * @param notification 알림 데이터
 * @constructor
 */
export default function NotificationItem({notification}: NotificationItemProps) {
    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
            <div className="flex justify-between items-start">
                <p className="text-gray-800">{notification.alarmText}</p>
                <span className="text-sm text-gray-500 ml-2 whitespace-nowrap">{notification.alarmTime}</span>
            </div>
        </div>
    )
}

interface NotificationItemProps {
    notification: Notification
}