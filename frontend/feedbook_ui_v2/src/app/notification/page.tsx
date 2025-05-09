import NotificationScrollList from "@/components/NotificationScrollList";

/**
 * 알림 페이지
 */
export default function NotificationPage() {
    return (
        <div className="flex-1 max-w-2xl mx-auto w-full p-4 pb-20">
            <h1 className="text-2xl font-bold mb-6">알림 내역</h1>
            <NotificationScrollList/>
        </div>
    )
}