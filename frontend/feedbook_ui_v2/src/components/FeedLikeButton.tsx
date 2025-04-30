"use client"

import {useState} from "react";
import {Heart} from "lucide-react";
import {cn} from "@/lib/utils";
import {useMutation} from "@tanstack/react-query";
import {updateFavorite} from "@/lib/feedApiCaller";

/**
 * 피드 좋아요 버튼 컴포넌트
 */
export default function FeedLikeButton({feedId, isLike, defaultCount, disabled = false}: FeedLikeButtonProps) {
    const [liked, setLiked] = useState(isLike)
    const [likeCount, setLikeCount] = useState(defaultCount)

    const feedFavoriteMutation = useMutation({
        mutationKey: ['updateFavorite', {feedId}],
        mutationFn: (id: number) => updateFavorite(id),
        onSuccess: () => {
            console.log(`Updated feed favorites, feedId = ${feedId}`)
        },
        onError: (error) => {
            console.log(`Failed update favorite feed, feedId = ${feedId}, cause = ${error.message}`)
        }
    })

    const handleLike = (): void => {
        if (!liked) {
            setLikeCount(likeCount + 1)
        } else {
            setLikeCount(Math.max(likeCount - 1, 0))
        }

        setLiked(!liked)
        feedFavoriteMutation.mutate(feedId)
    }

    return (
        <button className="flex items-center space-x-1 group" onClick={handleLike} disabled={disabled}>
            <Heart size={18} className={cn(
                "transition-colors",
                liked ? "fill-red-500 text-red-500" : "text-gray-500 group-hover:text-red-500"
            )}/>
            <span className="text-sm text-gray-600">{likeCount > 0 ? `+${likeCount}` : ''}</span>
        </button>
    )
}

interface FeedLikeButtonProps {
    feedId: number,
    isLike: boolean,
    defaultCount: number,
    disabled?: boolean
}