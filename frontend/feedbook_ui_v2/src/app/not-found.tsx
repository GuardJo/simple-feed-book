import NotFoundCard from "@/components/NotFoundCard";

/**
 * 존재하지 않는 페이지 접근 시 출력 페이지
 */
export default function NotFoundPage() {
    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <NotFoundCard/>
        </div>
    )
}