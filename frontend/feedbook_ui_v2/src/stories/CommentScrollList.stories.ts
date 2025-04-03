import {Meta, StoryObj} from "@storybook/react";
import CommentScrollList from "@/components/CommentScrollList";

const meta = {
    title: 'Components/CommentScrollList',
    component: CommentScrollList
} satisfies Meta<typeof CommentScrollList>

export default meta

type Story = StoryObj<typeof meta>

export const HasComments: Story = {
    args: {
        feedId: 1,
    }
}

export const EmptyComments: Story = {
    args: {
        feedId: 2, // mocking 된 API에선 1 이상의 ID값엔 빈 배열을 반환하도록 함
    }
}