import FeedScrollList from "@/components/FeedScrollList";

/**
 * 전체 피드 조회 페이지
 */
export default function AllFeedListPage() {
    return (
        <div className="flex-1 max-w-2xl mx-auto w-full p-4 pb-20">
            <h1 className="text-2xl font-bold mb-6">피드</h1>
            <FeedScrollList/>
        </div>
    )
}