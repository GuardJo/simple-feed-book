import FeedCreateForm from "@/components/FeedCreateForm";

/**
 * 신규 피드 작성 페이지
 */
export default function EditFeedPage() {
    return (
        <div className="flex-1 max-w-2xl mx-auto w-full p-4 pb-20">
            <h1 className="text-2xl font-bold mb-6">새 피드 작성</h1>
            <FeedCreateForm/>
        </div>
    )
}