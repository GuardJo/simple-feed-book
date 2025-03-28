import {Meta, StoryObj} from "@storybook/react";
import FeedLikeButton from "@/components/FeedLikeButton";

const meta = {
    title: "components/FeedLikeButton",
    component: FeedLikeButton
} satisfies Meta<typeof FeedLikeButton>

export default meta

type Story = StoryObj<typeof meta>

export const NoLike: Story = {
    args: {
        isLike: false,
        defaultCount: 0
    }
}

export const HasLike: Story = {
    args: {
        isLike: false,
        defaultCount: 999
    }
}

export const MyLike: Story = {
    args: {
        isLike: true,
        defaultCount: 111
    }
}