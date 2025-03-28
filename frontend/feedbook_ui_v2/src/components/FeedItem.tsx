import {Edit, Trash2} from "lucide-react";
import FeedLikeButton from "@/components/FeedLikeButton";
import {Feed} from "@/lib/feedApiCaller";

/**
 * 피드 컴포넌트
 */
export default function FeedItem({feed}: FeedItemProps) {
    const handleEditFeed = () => {
        console.log(`Edit feed, feedId = ${feed.id}`)
    }

    const handleRemoveFeed = () => {
        console.log(`Remove feed, feedId = ${feed.id}`)
    }

    return (
        <div className="rounded-lg border bg-white p-4 shadow-sm">
            <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-medium">{feed.title}</h3>
                <div className="flex space-x-2">
                    <button onClick={handleEditFeed} className="text-gray-500 hover:text-gray-700" aria-label="edit"
                            hidden={!feed.isOwner}>
                        <Edit size={18}/>
                    </button>
                    <button onClick={handleRemoveFeed} className="text-gray-600 hover:text-red-500" aria-label="remove"
                            hidden={!feed.isOwner}>
                        <Trash2 size={18}/>
                    </button>
                </div>
            </div>

            <div className="mb-2 text-sm text-gray-600">{feed.author}</div>

            <div className="mb-4">{feed.content}</div>

            <div className="flex justify-between items-center">
                <FeedLikeButton isLike={feed.isFavorite} defaultCount={feed.totalFavorites}/>
                <button className="text-sm text-blue-600 hover:underline">Comments...</button>
            </div>
        </div>
    )
}

interface FeedItemProps {
    feed: Feed
}