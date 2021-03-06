// PInternalState.h
//
// Copyright 1998-2006 by David K. McAllister.
//
// Defines these classes: PInternalState_t

#pragma once

#include "pAPI.h"
#include "ParticleActions.h"
#include "ParticleGroup.h"

#include <vector>
#include <string>

#define PASSERT(x,msg) {if(!(x)) { throw PErrInternalError(msg); }}

namespace PAPI {

    typedef std::vector<PActionBase *> ActionList;

    // This is the per-thread state of the API.
    // All API calls get their data from here.
    // In the non-multithreaded case there is one global instance of this class.
    class PInternalState_t
    {
    public:
        PInternalSourceState_t SrcSt; // Any particles created will get their attributes from here.

        float dt;
        bool in_call_list;
        bool in_new_list;

        std::vector<ParticleGroup> PGroups;
        int pgroup_id;

        std::vector<ActionList> ALists;
        int alist_id;

        // How many particles will fit in cache? You can set this if you don't like the default value.
        int PWorkingSetSize;

        PInternalState_t();

        int GeneratePGroups(int pgroups_requested);
        int GenerateALists(int alists_requested);

        // Action API entry points call this to either store the action in a list or execute and delete it.
        void SendAction(PActionBase *S);

        // Execute an action list
        void ExecuteActionList(ActionList &AList);
    };

};
