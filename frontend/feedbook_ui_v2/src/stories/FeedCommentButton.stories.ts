import {Meta, StoryObj} from "@storybook/react";
import FeedCommentButton from "@/components/FeedCommentButton";

const meta = {
    title: "Components/FeedCommentButton",
    component: FeedCommentButton,
} satisfies Meta<typeof FeedCommentButton>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        feedId: 1,
    }
}