/**
 * Progress circle bar 컴포넌트
 */
export default function Spinner() {
    return (
        <div className="flex justify-center items-center">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-gray-900"/>
        </div>
    )
}