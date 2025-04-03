import {Meta, StoryObj} from "@storybook/react";
import CommentScrollList from "@/components/CommentScrollList";

const meta = {
    title: 'Components/CommentScrollList',
    component: CommentScrollList
} satisfies Meta<typeof CommentScrollList>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        feedId: 1,
    }
}