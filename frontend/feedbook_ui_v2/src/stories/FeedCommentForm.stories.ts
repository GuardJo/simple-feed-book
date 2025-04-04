import {Meta, StoryObj} from "@storybook/react";
import FeedCommentForm from "@/components/FeedCommentForm";

const meta = {
    title: 'Components/FeedCommentForm',
    component: FeedCommentForm,
} satisfies Meta<typeof FeedCommentForm>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        feedId: 1,
    }
}