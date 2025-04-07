import {FeedComment} from "@/lib/feedApiCaller";

/**
 * 피드 댓글 컴포넌트
 */
export default function FeedCommentItem({comment}: CommentItemProps) {
    return (
        <div className="flex items-start gap-3">
            <div className="w-8 h-8 rounded-full bg-gray-200 flex-shrink-0 flex items-center justify-center">
                {comment.author.charAt(0).toUpperCase()}
            </div>
            <div className="flex-1">
                <div className="flex items-baseline gap-2">
                    <span className="font-medium">{comment.author}</span>
                    <span className="text-xs text-gray-500">{comment.createTime}</span>
                </div>
                <p className="text-gray-800">{comment.content}</p>
            </div>
        </div>
    )
}

interface CommentItemProps {
    comment: FeedComment
}