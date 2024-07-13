package com.example.window;

interface ISurfaceSyncGroup {
    boolean onAddedToSyncGroup(in IBinder parentSyncGroupToken, boolean parentSyncGroupMerge);
    boolean addToSync(in ISurfaceSyncGroup surfaceSyncGroup, boolean parentSyncGroupMerge);
}