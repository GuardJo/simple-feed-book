/**
 * 메인 페이지
 */
export default function Home() {
    return (
        <div className="flex-1 flex flex-col justify-center items-center p-4">
            <div className="flex-1 flex flex-col justify-center items-center">
                <h1 className="text-5xl md:text-6xl font-bold text-center">Feedbook</h1>
            </div>
            <div className="w-full text-center pb-8">
                <p className="text-sm text-gray-500">Copyright. Guardjo</p>
                <p className="text-xs text-gray-400 mt-1">Version 1.0.0</p>
            </div>
        </div>
    )
}
