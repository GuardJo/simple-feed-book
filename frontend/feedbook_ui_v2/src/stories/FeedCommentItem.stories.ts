import {Meta, StoryObj} from "@storybook/react";
import FeedCommentItem from "@/components/FeedCommentItem";

const meta = {
    title: "Components/FeedCommentItem",
    component: FeedCommentItem
} satisfies Meta<typeof FeedCommentItem>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        comment: {
            id: 1,
            author: 'Tester',
            createTime: '2025-04-03 13:23',
            content: '댓글 테스트'
        }
    }
}