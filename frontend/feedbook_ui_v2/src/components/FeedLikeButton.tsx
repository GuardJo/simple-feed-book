"use client"

import {useState} from "react";
import {Heart} from "lucide-react";
import {cn} from "@/lib/utils";

/**
 * 피드 좋아요 버튼 컴포넌트
 */
export default function FeedLikeButton({isLike, defaultCount}: FeedLikeButtonProps) {
    const [liked, setLiked] = useState(isLike)
    const [likeCount, setLikeCount] = useState(defaultCount)

    const handleLike = (): void => {
        if (!liked) {
            setLikeCount(likeCount + 1)
        } else {
            setLikeCount(Math.max(likeCount - 1, 0))
        }
        
        setLiked(!liked)
    }

    return (
        <button className="flex items-center space-x-1 group" onClick={handleLike}>
            <Heart size={18} className={cn(
                "transition-colors",
                liked ? "fill-red-500 text-red-500" : "text-gray-500 group-hover:text-red-500"
            )}/>
            <span className="text-sm text-gray-600">{likeCount > 0 ? `+${likeCount}` : ''}</span>
        </button>
    )
}

interface FeedLikeButtonProps {
    isLike: boolean,
    defaultCount: number,
}